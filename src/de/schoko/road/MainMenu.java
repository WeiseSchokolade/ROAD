package de.schoko.road;

import java.awt.Color;
import java.io.IOException;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.TextAlignment;
import de.schoko.rendering.panels.PanelSystem;
import de.schoko.road.multiplayer.MultiPlayerMenu;
import de.schoko.saving.config.ResourceLocation;
import de.schoko.uitil.TextInputBox;

public class MainMenu extends Menu {
	
	private TextButton singlePlayerButton;
	private TextButton multiPlayerButton;
	private TextButton settingsButton;
	
	private TextInputBox nameInput;
	
	@Override
	public void onChange() {
		getContext().getPanelSystem().clear();
	}
	
	@Override
	public void onLoad(Context context) {
		PanelSystem panelSystem = context.getPanelSystem();
		nameInput = new TextInputBox(0, 0, Constants.PLAYER_NAME, 30);
		panelSystem.add(nameInput);
		
		context.getSettings().setRenderCoordinateSystem(false);
		context.getSettings().setBackgroundColor(Constants.MAIN_MENU_BACKGROUND_COLOR);
		singlePlayerButton = new TextButton(context, "Singleplayer", 0, 0, Color.RED, Constants.MAIN_MENU_FONT,
				Constants.MAIN_MENU_BACKGROUND_COLOR, Constants.MAIN_MENU_BACKGROUND_COLOR, Color.WHITE, Color.LIGHT_GRAY);
		multiPlayerButton = new TextButton(context, "Multiplayer", 0, 0, Color.RED, Constants.MAIN_MENU_FONT,
				Constants.MAIN_MENU_BACKGROUND_COLOR, Constants.MAIN_MENU_BACKGROUND_COLOR, Color.WHITE, Color.LIGHT_GRAY);
		settingsButton = new TextButton(context, "Settings", 30, 30, Color.RED, Constants.MAIN_MENU_FONT,
				Constants.MAIN_MENU_BACKGROUND_COLOR, Constants.MAIN_MENU_BACKGROUND_COLOR, Color.WHITE, Color.LIGHT_GRAY);
	}
	
	@Override
	public void update(double deltaTime) {
		if (singlePlayerButton.wasReleased()) {
			RoadProject.get().setMenu(new SinglePlayerMenu());
			return;
		}
		if (multiPlayerButton.wasReleased()) {
			RoadProject.get().setMenu(new MultiPlayerMenu());
			return;
		}
		if (settingsButton.wasReleased()) {
			RoadProject.get().setMenu(new SettingsMenu(this));
		}
		if (nameInput.shouldApply()) {
			Constants.PLAYER_NAME = nameInput.getString();
			RoadProject.get().getSettingsConfig().set("name", Constants.PLAYER_NAME);
			try {
				RoadProject.get().getSettingsConfig().write("settings.txt", ResourceLocation.FILE);
			} catch (IOException e) {
				System.err.println("Couldn't save settings!");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void render(Graph g) {
		HUDGraph hud = g.getHUD();
		hud.drawText("R.O.A.D.", hud.getWidth() / 2, hud.getHeight() / 4, Color.WHITE, Constants.MAIN_MENU_FONT_BIG, TextAlignment.CENTER);
		hud.drawText("Racing objecting all decrees", hud.getWidth() / 2, hud.getHeight() / 4 + Constants.MAIN_MENU_SMALL_FONT.getSize() + 5,
				Color.WHITE, Constants.MAIN_MENU_SMALL_FONT, TextAlignment.CENTER);
		
		singlePlayerButton.setX((int) (hud.getWidth() / 4), TextAlignment.CENTER);
		singlePlayerButton.setY((int) (hud.getHeight() / 2) - singlePlayerButton.getHeight());
		hud.draw(singlePlayerButton);
		
		multiPlayerButton.setX((int) (3 * hud.getWidth() / 4), TextAlignment.CENTER);
		multiPlayerButton.setY((int) (hud.getHeight() / 2) - multiPlayerButton.getHeight());
		hud.draw(multiPlayerButton);
		
		settingsButton.setX((int) (hud.getWidth() - 30), TextAlignment.RIGHT);
		hud.draw(settingsButton);
		
		nameInput.setX((int) (hud.getWidth() / 2) - nameInput.getWidth() / 2);
		nameInput.setY((int) (hud.getHeight() / 3));
	}
}
