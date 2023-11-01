package de.schoko.road;

import java.awt.Color;
import java.io.IOException;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.Keyboard;
import de.schoko.rendering.TextAlignment;
import de.schoko.saving.config.Config;
import de.schoko.saving.config.ResourceLocation;
import de.schoko.uitil.InputBox;
import de.schoko.uitil.IntInputBox;
import de.schoko.uitil.TextInputBox;

public class SettingsMenu extends Menu {
	private Menu previousMenu;
	
	private TextButton renderQualityButton;
	private int currentQuality = 2;
	private RenderQuality[] qualities = RenderQuality.values();
	
	private TextButton showNamesButton;
	private TextButton showFramesButton;
	private TextButton exitButton;
	private TextInputBox ipInputField;
	private IntInputBox portInputField;
	
	public SettingsMenu(Menu previousMenu) {
		this.previousMenu = previousMenu;
	}
	
	@Override
	public void onLoad(Context context) {
		context.getSettings().setBackgroundColor(Constants.MAIN_MENU_BACKGROUND_COLOR);
		context.getSettings().setAutoCam(false);
		context.getSettings().setRenderCoordinateSystem(false);
		
		renderQualityButton = new TextButton(context, "Quality: " + Constants.RENDER_QUALITY.getName(), 30, 90, Color.RED, Constants.MAIN_MENU_FONT,
				Constants.MAIN_MENU_BACKGROUND_COLOR, Constants.MAIN_MENU_BACKGROUND_COLOR, Color.WHITE, Color.LIGHT_GRAY);
		showNamesButton = new TextButton(context, "Show Names: " + Constants.SHOW_NAMES, 30, 180, Color.RED, Constants.MAIN_MENU_FONT,
				Constants.MAIN_MENU_BACKGROUND_COLOR, Constants.MAIN_MENU_BACKGROUND_COLOR, Color.WHITE, Color.LIGHT_GRAY);
		showFramesButton = new TextButton(context, "Show Frames: " + Constants.SHOW_FRAMES, 30, 230, Color.RED, Constants.MAIN_MENU_FONT,
				Constants.MAIN_MENU_BACKGROUND_COLOR, Constants.MAIN_MENU_BACKGROUND_COLOR, Color.WHITE, Color.LIGHT_GRAY);
		exitButton = new TextButton(context, "Close Settings", 30, 0, Color.RED, Constants.MAIN_MENU_FONT,
				Constants.MAIN_MENU_BACKGROUND_COLOR, Constants.MAIN_MENU_BACKGROUND_COLOR, Color.WHITE, Color.LIGHT_GRAY);
		ipInputField = new TextInputBox(context, 80, 280, Constants.SERVER_IP, 999);
		portInputField = new IntInputBox(context, 110, 320, Constants.SERVER_PORT, 1, 65536);
	}
	
	@Override
	public void update(double deltaTime) {
		Keyboard keyboard = getContext().getKeyboard();
		Config settingsConfig = RoadProject.get().getSettingsConfig();
		
		if ((keyboard.wasRecentlyPressed(Keyboard.ESCAPE) && InputBox.getSelectedInputBox() == null) || exitButton.wasReleased()) {
			if (settingsConfig.isChanged()) {
				try {
					settingsConfig.write("settings.txt", ResourceLocation.FILE);
				} catch (IOException e) {
					System.err.println("Couldn't save settings!");
					e.printStackTrace();
				}
			}
			RoadProject.get().setMenu(previousMenu);
			return;
		}
		
		if (renderQualityButton.wasReleased()) {
			currentQuality++;
			if (currentQuality >= qualities.length) currentQuality = 0;
			Constants.RENDER_QUALITY = qualities[currentQuality];
			renderQualityButton.setText("Quality: " + Constants.RENDER_QUALITY.getName());
			settingsConfig.set("quality", Constants.RENDER_QUALITY.getName());
		}
		
		if (showNamesButton.wasReleased()) {
			Constants.SHOW_NAMES = !Constants.SHOW_NAMES;
			showNamesButton.setText("Show Names: " + Constants.SHOW_NAMES);
			settingsConfig.set("showNames", String.valueOf(Constants.SHOW_NAMES));
		}

		if (showFramesButton.wasReleased()) {
			Constants.SHOW_FRAMES = !Constants.SHOW_FRAMES;
			showFramesButton.setText("Show Frames: " + Constants.SHOW_FRAMES);
			settingsConfig.set("showFrames", String.valueOf(Constants.SHOW_FRAMES));
		}
		
		if (ipInputField.shouldApply()) {
			Constants.SERVER_IP = ipInputField.getString();
			ipInputField.setString(Constants.SERVER_IP);
			settingsConfig.set("ip", Constants.SERVER_IP);
		}
		
		if (portInputField.shouldApply()) {
			Constants.SERVER_PORT = portInputField.getNum();
			portInputField.setNum(Constants.SERVER_PORT);
			settingsConfig.set("port", String.valueOf(Constants.SERVER_PORT));
		}
	}

	@Override
	public void render(Graph g) {
		HUDGraph hud = g.getHUD();
		
		hud.draw(renderQualityButton);
		hud.drawText(Constants.RENDER_QUALITY.getDescription(), 30, 145, Color.RED, Constants.MAIN_MENU_SMALL_FONT, TextAlignment.LEFT);
		hud.drawText("Note: The actual quality depends on your hardware", 30, 150 + Constants.MAIN_MENU_SMALL_FONT.getSize(), Color.RED, Constants.MAIN_MENU_SMALL_FONT, TextAlignment.LEFT);
		
		hud.draw(showNamesButton);
		
		hud.draw(showFramesButton);
		
		exitButton.setY((int) (hud.getHeight() - exitButton.getHeight() - 30));
		hud.draw(exitButton);
		
		hud.drawText("IP:", 30, 305, Color.RED, Constants.MAIN_MENU_FONT, TextAlignment.LEFT);
		hud.draw(ipInputField);
		hud.drawText("Port:", 30, 345, Color.RED, Constants.MAIN_MENU_FONT, TextAlignment.LEFT);
		hud.draw(portInputField);
	}
}
