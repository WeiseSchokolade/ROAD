package de.schoko.road.multiplayer;

import java.awt.Color;
import java.io.IOException;
import java.net.ConnectException;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.TextAlignment;
import de.schoko.road.Constants;
import de.schoko.road.Menu;
import de.schoko.road.RoadProject;
import de.schoko.road.TextButton;
import de.schoko.road.server.shared.SharedConstants;
import de.schoko.road.server.shared.packets.GameStartPacket;
import de.schoko.road.server.shared.packets.HeaderPacket;
import de.schoko.road.server.shared.packets.LobbyReadyPacket;
import de.schoko.road.server.shared.packets.LobbyStatusPacket;
import de.schoko.road.server.shared.packets.Packet;

public class ConnectionMenu extends Menu {
	private Client client;
	private String map;
	private boolean ready;
	private TextButton readyButton;
	
	private int readyAmount;
	private int totalPlayerAmount;
	private String[] mapPlayers;
	
	public ConnectionMenu(String map) {
		this.map = map;
		mapPlayers = new String[0];
	}
	
	@Override
	public void onLoad(Context context) {
		try {
			client = new Client(Constants.SERVER_IP, Constants.SERVER_PORT);
			client.send(new HeaderPacket(SharedConstants.PROTOCOL_VERSION, SharedConstants.EDITION, Constants.PLAYER_NAME, Constants.CAR_MODEL.getImageName(), map));
		} catch (ConnectException e) {
			System.out.println("Couldn't connect to server: " + e.getMessage());
			RoadProject.get().setMenu(new MultiPlayerMenu("Couldn't connect to server. Check your ip/port in the settings."));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Couldn't connect to server");
			e.printStackTrace();
			RoadProject.get().setMenu(new MultiPlayerMenu());
		}
		readyButton = new TextButton(context, "Ready: " + ready, 0, 0, Color.RED, Constants.MAIN_MENU_FONT,
				Constants.MAIN_MENU_BACKGROUND_COLOR, Constants.MAIN_MENU_BACKGROUND_COLOR, Color.WHITE, Color.LIGHT_GRAY);
	}

	@Override
	public void update(double deltaTime) {
		if (readyButton.wasReleased()) {
			ready = !ready;
			client.send(new LobbyReadyPacket(ready));
			readyButton.setText("Ready: " + ready);
		}
		
		String read = client.read();
		if (read != null) {
			Packet packet = Packet.getGson().fromJson(read, Packet.class);
			switch (packet.getType()) {
			case "LobbyStatusPacket":
				LobbyStatusPacket p0 = Packet.getGson().fromJson(read, LobbyStatusPacket.class);
				readyAmount = p0.readyAmount;
				totalPlayerAmount = p0.totalPlayers;
				mapPlayers = p0.players;
				break;
			case "GameStartPacket":
				RoadProject.get().setMenu(new GameLoadMenu(client, Packet.getGson().fromJson(read, GameStartPacket.class)));
				break;
			default:
				System.out.println("Unknown packet: " + read);
				break;
			}
		}
	}
	
	@Override
	public void render(Graph g) {
		HUDGraph hud = g.getHUD();
		readyButton.setX((int) ((hud.getWidth() - readyButton.getWidth()) / 2));
		readyButton.setY((int) (hud.getHeight() / 2));
		hud.draw(readyButton);
		
		if (totalPlayerAmount < 2) {
			hud.drawText("Game needs at least two players to start!", hud.getWidth() / 2, hud.getHeight() / 2 + readyButton.getHeight() + Constants.MAIN_MENU_SMALL_FONT.getSize(), Color.RED, Constants.MAIN_MENU_SMALL_FONT, TextAlignment.CENTER);
		}
		
		hud.drawText("Map: " + map, (int) (hud.getWidth() / 2), 45, Color.RED, Constants.MAIN_MENU_FONT, TextAlignment.CENTER);
		
		double playerListX = hud.getWidth() / 5;
		hud.drawText("Players - Ready: " + readyAmount + "/" + mapPlayers.length, playerListX, 120, Color.RED, Constants.MAIN_MENU_FONT, TextAlignment.CENTER);
		for (int i = 0; i < mapPlayers.length; i++) {
			hud.drawText(mapPlayers[i], playerListX + 5, 150 + i * 30, Color.WHITE, Constants.MAIN_MENU_FONT, TextAlignment.CENTER);
		}
		
		hud.drawText("Total Players: " + totalPlayerAmount, hud.getWidth() * 4 / 5, 120, Color.RED, Constants.MAIN_MENU_FONT, TextAlignment.CENTER);
	}

}
