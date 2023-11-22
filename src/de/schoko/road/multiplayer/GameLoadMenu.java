package de.schoko.road.multiplayer;

import java.awt.Color;

import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.TextAlignment;
import de.schoko.road.Constants;
import de.schoko.road.Map;
import de.schoko.road.Maps;
import de.schoko.road.Menu;
import de.schoko.road.RoadProject;
import de.schoko.road.game.CoreGame;
import de.schoko.road.game.PlayerCar;
import de.schoko.road.game.RemoteCar;
import de.schoko.road.layers.RoadLayer;
import de.schoko.road.server.shared.packets.GameStartPacket;

public class GameLoadMenu extends Menu {
	private Client client;
	private GameStartPacket data;
	private Map map;
	
	public GameLoadMenu(Client client, GameStartPacket data) {
		this.client = client;
		this.data = data;
		System.out.println("PlayerID: " + data.playerID);
		System.out.println("Starting game at: " + data.startTime);
	}
	
	@Override
	public void update(double deltaTime) {
		if (System.currentTimeMillis() >= data.startTime - 9000 && map == null) {
			map = Maps.loadMapFromData(data.mapData);
			RoadLayer layer = new RoadLayer(map);
			layer.onLoad(getContext());
		}
		client.read();
		if (System.currentTimeMillis() >= data.startTime - 7000) {
			CoreGame game = new CoreGame(map);
			client.clear();
			String[] players = data.players;
			for (int i = 0; i < players.length; i++) {
				if (players[i].isBlank()) continue;
				if (i == data.playerID) {
					PlayerCar car = new PlayerCar(getContext(), game.getRoadLayer());
					game.addCar(car, data.playerID);
					System.out.println("Adding player car on slot " + data.playerID);
				} else {
					game.addCar(new RemoteCar(players[i], getContext(), game.getRoadLayer()), i);
					System.out.println("Adding remote car on slot " + i);
				}
			}
			RoadProject.get().setMenu(new MultiplayerGameMenu(game, client, data));
			return;
		}
	}

	@Override
	public void render(Graph g) {
		HUDGraph hud = g.getHUD();
		hud.drawText("Joining Game...", hud.getWidth() / 2, hud.getHeight() / 2, Color.WHITE, Constants.MAIN_MENU_FONT, TextAlignment.CENTER);
		if (map != null) {
			g.drawImage(map.getMapImage(), 100000, 100000, 0.3);
		}
	}
	
}
