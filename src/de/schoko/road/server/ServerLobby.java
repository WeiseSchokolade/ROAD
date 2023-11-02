package de.schoko.road.server;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import de.schoko.road.server.shared.DisconnectionReason;
import de.schoko.road.server.shared.SharedConstants;
import de.schoko.road.server.shared.packets.DisconnectionPacket;
import de.schoko.road.server.shared.packets.HeaderPacket;
import de.schoko.road.server.shared.packets.LobbyReadyPacket;
import de.schoko.road.server.shared.packets.LobbyStatusPacket;
import de.schoko.road.server.shared.packets.Packet;
import de.schoko.serverbase.Application;
import de.schoko.serverbase.Server;
import de.schoko.serverbase.core.Connection;

public class ServerLobby extends Application {
	private static final String[] KNOWN_MAPS = {
			"track_1",
			"track_2",
			"track_3"};
	
	private Server server;
	private ArrayList<LobbyConnection> connections;
	private HashMap<String, MapStatus> maps;
	
	public ServerLobby(Server server) {
		super("Road Server Lobby", false);
		this.server = server;
		connections = new ArrayList<>();
		maps = new HashMap<>();
		for (int i = 0; i < KNOWN_MAPS.length; i++) {
			maps.put(KNOWN_MAPS[i], new MapStatus(KNOWN_MAPS[i]));
		}
	}
	
	@Override
	public void update(double deltaTimeMS) {
		Gson gson = Packet.getGson();
		for (int i = 0; i < connections.size(); i++) {
			LobbyConnection connection = connections.get(i);
			if (connection.isClosed()) {
				connections.remove(i);
				i--;
				if (connection.getMap() != null) sendMapUpdate(connection.getMap());
				continue;
			}
			String read = connection.read();
			if (read == null) continue;
			try {
				Packet defaultPacket = gson.fromJson(read, Packet.class);
				switch (defaultPacket.getType()) {
				case "HeaderPacket":
					HeaderPacket p0 = gson.fromJson(read, HeaderPacket.class);
					if (p0.version != SharedConstants.PROTOCOL_VERSION) {
						connection.send(new DisconnectionPacket(DisconnectionReason.DIFFERENT_PROTOCOL_VERSION));
						connection.close();
						break;
					}
					System.out.println("Connection: " + read);
					connection.setVersion(p0.version);
					connection.setName(p0.name);
					connection.setMap(p0.map);
					if (mapExists(p0.map)) {
						MapStatus map = maps.get(p0.map);
						map.addPlayer(connection);
						sendMapUpdate(connection.getMap());
					} else {
						connection.send(new DisconnectionPacket(DisconnectionReason.ILLEGAL_VALUE, "map"));
						connection.close();
						break;
					}
					break;
				case "LobbyReadyPacket":
					LobbyReadyPacket p1 = gson.fromJson(read, LobbyReadyPacket.class);
					connection.setReady(p1.ready);
					sendMapUpdate(connection.getMap());
					break;
				default:
					System.out.println("Unknown packet: " + defaultPacket.getType());
					break;
				}
			} catch (JsonSyntaxException e) {
				System.out.println("Couldn't parse JSON: " + read);
				e.printStackTrace();
			}
		}
		maps.values().forEach((mapStatus) -> {
			mapStatus.update();
			if (mapStatus.isReady()) {
				String[] playerNames = mapStatus.getPlayerNames();
				ArrayList<LobbyConnection> readyPlayers = mapStatus.getReadyPlayers();
				Game game = new Game(server, mapStatus.getName(), playerNames);
				for (int i = 0; i < readyPlayers.size(); i++) {
					connections.remove(readyPlayers.get(i));
					game.addConnection(readyPlayers.get(i).getConnection());
				}
				System.out.println("Starting new game");
				server.startApplication(game);
			}
		});
	}
	
	public boolean mapExists(String map) {
		for (int i = 0; i < KNOWN_MAPS.length; i++) {
			if (KNOWN_MAPS[i].equals(map)) {
				return true;
			}
		}
		return false;
	}
	
	public void sendMapUpdate(String mapName) {
		MapStatus map = maps.get(mapName);
		sendAllConnectionsWithMap(mapName, new LobbyStatusPacket(map.getReadyAmount(), map.getPlayerNames(), connections.size()));
	}
	
	public void sendAllConnectionsWithMap(String mapSelection, Packet packet) {
		connections.forEach((connection) -> {
			if (connection.getMap().equals(mapSelection)) {
				connection.send(packet);
			}
		});
	}
	
	@Override
	public void addConnection(Connection connection) {
		super.addConnection(connection);
		connections.add(new LobbyConnection(connection));
	}
}
