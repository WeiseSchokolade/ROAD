package de.schoko.road.server;

import java.util.ArrayList;

import com.google.gson.JsonSyntaxException;

import de.schoko.road.server.shared.packets.CarInfoPacket;
import de.schoko.road.server.shared.packets.GamePlayersUpdatePacket;
import de.schoko.road.server.shared.packets.GameStartPacket;
import de.schoko.road.server.shared.packets.Packet;
import de.schoko.serverbase.Application;
import de.schoko.serverbase.Server;
import de.schoko.serverbase.core.Connection;

public class Game extends Application {
	private static final String[] KNOWN_IMAGES = {
			"car",
			"car_yellow",
			"car_red"
	};
	private String mapData;
	private String[] playerNames;

	private long startTime;
	private boolean started;

	private ArrayList<GameConnection> connections;

	public Game(Server server, String mapData, String[] playerNames) {
		super("Road Game", true);
		this.playerNames = playerNames;
		this.mapData = mapData;
		this.connections = new ArrayList<>();
		startTime = System.currentTimeMillis() + 10000;
	}

	@Override
	public void update(double deltaTimeMS) {
		if (System.currentTimeMillis() >= startTime && !started) {
			started = true;
			System.out.println("Started at " + System.currentTimeMillis());
		}
		for (int i = 0; i < connections.size(); i++) {
			GameConnection connection = connections.get(i);
			String read;
			while ((read = connection.read()) != null) {
				handlePacket(connection, read);
			}
		}

		CarInfoPacket[] infoPacket = new CarInfoPacket[connections.size()];
		for (int i = 0; i < infoPacket.length; i++) {
			infoPacket[i] = new CarInfoPacket(connections.get(i));
		}
		sendAll(Packet.toJson(new GamePlayersUpdatePacket(infoPacket)));
	}

	public void handlePacket(GameConnection connection, String read) {
		try {
			Packet packet = Packet.getGson().fromJson(read, Packet.class);
			switch (packet.getType()) {
			case "CarInfoPacket":
				CarInfoPacket p0 = Packet.getGson().fromJson(read, CarInfoPacket.class);
				if (started) {
					connection.setPos(p0.pos);
					connection.setDir(p0.dir);
					connection.setSpeed(p0.speed);
					connection.setRotationSpeed(p0.rotationSpeed);
					connection.setSpeedVec(p0.speedVec);
					connection.setAcceleration(p0.acceleration);
					connection.setRound(p0.round);
					connection.setProgress(p0.progress);
					connection.setCompleted(p0.completed);
					connection.setCompleteTime(p0.completeTime);
					connection.setLastUpdate(p0.lastUpdate);
					if (imageExists(p0.carImageName)) {
						connection.setCarImageName(p0.carImageName);
					}
				} else {
					connection.setPos(p0.pos);
					if (imageExists(p0.carImageName)) {
						connection.setCarImageName(p0.carImageName);
					}
					connection.send(new CarInfoPacket(connection));
				}
				break;
			default:
				System.out.println("Unknown packet: " + read);
				break;
			}
		} catch (JsonSyntaxException e) {
			System.out.println("Couldn't read json: " + read);
		}
	}

	public boolean imageExists(String image) {
		for (int i = 0; i < KNOWN_IMAGES.length; i++) {
			if (KNOWN_IMAGES[i].equals(image)) {
				return true;
			}
		}
		return false;
	}


	@Override
	public void addConnection(Connection connection) {
		super.addConnection(connection);
		connections.add(new GameConnection(connection, getConnections().size() - 1));
		connection.send(Packet.toJson(new GameStartPacket(playerNames, mapData, getConnections().size() - 1, startTime)));
	}
}
