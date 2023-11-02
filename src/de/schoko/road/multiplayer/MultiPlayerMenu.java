package de.schoko.road.multiplayer;

import java.awt.Color;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.TextAlignment;
import de.schoko.road.CarSelectionButton;
import de.schoko.road.Constants;
import de.schoko.road.MainMenu;
import de.schoko.road.Menu;
import de.schoko.road.RoadProject;
import de.schoko.road.TextButton;
import de.schoko.road.server.shared.SharedConstants;

public class MultiPlayerMenu extends Menu {
	private TextButton playButton;
	private TextButton mapButton;
	private TextButton backButton;
	private CarSelectionButton carSelectionButton;
	private String message;
	
	private int currentMap;
	
	public MultiPlayerMenu() {
	}
	
	public MultiPlayerMenu(String message) {
		this.message = message;
	}
	
	@Override
	public void onLoad(Context context) {
		context.getSettings().setRenderCoordinateSystem(false);
		context.getSettings().setBackgroundColor(59, 59, 59);
		
		playButton = new TextButton(context, "Play", 5, 0, Color.RED, Constants.MAIN_MENU_FONT, Constants.MAIN_MENU_BACKGROUND_COLOR, Constants.MAIN_MENU_BACKGROUND_COLOR, Color.WHITE, Color.LIGHT_GRAY);
		mapButton = new TextButton(context, "Map: " + SharedConstants.MAP_NAMES[currentMap], 0, 0, Color.RED, Constants.MAIN_MENU_FONT, Constants.MAIN_MENU_BACKGROUND_COLOR, Constants.MAIN_MENU_BACKGROUND_COLOR, Color.WHITE, Color.LIGHT_GRAY);
		backButton = new TextButton(context, "Back", 5, 0, Color.RED, Constants.MAIN_MENU_FONT, Constants.MAIN_MENU_BACKGROUND_COLOR, Constants.MAIN_MENU_BACKGROUND_COLOR, Color.WHITE, Color.LIGHT_GRAY);
		carSelectionButton = new CarSelectionButton(context);
	}
	
	@Override
	public void update(double deltaTime) {
		if (playButton.wasReleased()) {
			RoadProject.get().setMenu(new ConnectionMenu(SharedConstants.MAP_NAMES[currentMap]));
			return;
		}
		if (mapButton.wasReleased()) {
			currentMap++;
			if (currentMap >= SharedConstants.MAP_NAMES.length) currentMap = 0;
			mapButton.setText("Map: " + SharedConstants.MAP_NAMES[currentMap]);
			return;
		}
		if (backButton.wasReleased()) {
			RoadProject.get().setMenu(new MainMenu());
			return;
		}
	}
	
	@Override
	public void render(Graph g) {
		HUDGraph hud = g.getHUD();

		playButton.setX((int) (hud.getWidth() / 2), TextAlignment.CENTER);
		playButton.setY((int) (hud.getHeight() / 4));
		hud.draw(playButton);
		
		if (message != null) {
			hud.drawText(message, hud.getWidth() / 2, hud.getHeight() / 4 + playButton.getHeight() + Constants.MAIN_MENU_SMALL_FONT.getSize(),
					Color.RED, Constants.MAIN_MENU_SMALL_FONT, TextAlignment.CENTER);
		}
		
		mapButton.setX((int) (hud.getWidth() / 4), TextAlignment.CENTER);
		mapButton.setY((int) (hud.getHeight() / 2));
		hud.draw(mapButton);
		
		backButton.setY((int) (hud.getHeight() - backButton.getHeight() - 6));
		hud.draw(backButton);

		hud.draw(carSelectionButton);
	}
}
