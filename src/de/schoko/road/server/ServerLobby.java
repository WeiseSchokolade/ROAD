package de.schoko.road.server;

import java.util.ArrayList;

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
import de.schoko.utility.Logging;

public class ServerLobby extends Application {
	private Server server;
	private ArrayList<Room> rooms;
	private ArrayList<LobbyConnection> connections;
	private String[] mapNames;

	/**
	 * Time in milliseconds until client gets disconnected if no header is sent.
	 */
	private static final long CLIENT_TIMEOUT_UNTIL_HEADER = 5000;

	public ServerLobby(Server server, String[] mapNames) {
		super("Road Server Lobby", false);
		this.server = server;
		this.mapNames = mapNames;
		rooms = new ArrayList<>();
		connections = new ArrayList<>();
	}

	@Override
	public void update(double deltaTimeMS) {
		Gson gson = new Gson();
		long currentTime = System.currentTimeMillis();

		for (int i = 0; i < connections.size(); i++) {
			LobbyConnection connection = connections.get(i);
			if (!connection.hasSentHeader() && currentTime > connection.getConnectionTime() + CLIENT_TIMEOUT_UNTIL_HEADER) {
				// Connection is deemed not a ROAD Program and no disconnection packet is sent
				// because it's not expected that the client will understand it.
				Logging.logError("Client timeouted: " + connection.getConnection().getUID());
				connection.close();
			}
			if (connection.isClosed()) {
				connections.remove(i);
				i--;
				Logging.logInfo("Client disconnected: " + connection.getConnection().getUID());
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
					if (p0.edition != null) {
						if (!p0.edition.equals(SharedConstants.EDITION)) {
							connection.send(new DisconnectionPacket(DisconnectionReason.DIFFERENT_PROTOCOL_VERSION, "ServerEdition" + SharedConstants.EDITION));
							connection.close();
							break;
						}
					} else {
						connection.send(new DisconnectionPacket(DisconnectionReason.ILLEGAL_VALUE, "EditionMissing"));
						connection.close();
						break;
					}
					Logging.logInfo("New Connection: " + read);
					connection.setSentHeader(true);
					connection.setVersion(p0.version);
					connection.setName(p0.name);
					addToRoom(connection);
					break;
				case "LobbyReadyPacket":
					if (!connection.hasSentHeader()) {
						connection.send(new DisconnectionPacket(DisconnectionReason.ILLEGAL_ACTION, "MissingHeader"));
						connection.close();
						break;
					}
					LobbyReadyPacket p1 = gson.fromJson(read, LobbyReadyPacket.class);
					connection.setReady(p1.ready);
					break;
				default:
					Logging.logInfo("Unknown packet: " + defaultPacket.getType());
					break;
				}
			} catch (JsonSyntaxException e) {
				Logging.logError("Couldn't parse JSON: " + read);
				e.printStackTrace();
			}
		}
		for (int i = 0; i < rooms.size(); i++) {
			Room room = rooms.get(i);
			room.update();
			if (room.isReady()) {
				rooms.remove(i);
				i--;
				String[] playerNames = room.getPlayerNames();
				ArrayList<LobbyConnection> players = room.getConnections();
				Game game = new Game(server, room.getMap(), playerNames);
				players.forEach((connection) -> {
					connections.remove(connection);
					game.addConnection(connection.getConnection());
				});
				Logging.logInfo("Starting new game");
				server.startApplication(game);
			}
		}
	}

	public void addToRoom(LobbyConnection connection) {
		for (int i = 0; i < rooms.size(); i++) {
			Room room = rooms.get(i);
			if (room.hasSpace()) {
				room.addConnection(connection);
				return;
			}
		}
		Room room = new Room(mapNames);
		rooms.add(room);
		room.addConnection(connection);
	}

	public void sendUpdate(LobbyConnection connection) {
		connection.send(new LobbyStatusPacket(0, mapNames, mapNames, 0));
	}

	@Override
	public void addConnection(Connection connection) {
		super.addConnection(connection);
		connections.add(new LobbyConnection(connection));
	}

}