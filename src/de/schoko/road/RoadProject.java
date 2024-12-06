package de.schoko.road;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.Image;
import de.schoko.rendering.ImageLocation;
import de.schoko.rendering.Renderer;
import de.schoko.rendering.RendererSettings;
import de.schoko.rendering.TextAlignment;
import de.schoko.rendering.Window;
import de.schoko.saving.config.Config;
import de.schoko.saving.config.ConfigSettings;
import de.schoko.saving.config.ResourceLocation;
import de.schoko.utility.TimeLogger;

public class RoadProject extends Renderer {
	public static void main(String[] args) {
		instance = new RoadProject();

		ConfigSettings.getGlobal().setBaseFilePath(System.getenv("APPDATA") + File.separator + "road" + "/");
		ConfigSettings.getGlobal().setBaseResourcePath(Constants.RESOURCE_PATH);
		
		// Load settings
		loadSettings();
		
		// Load Maps
		ResourceCopy resourceCopy = new ResourceCopy();
		
		String parentDir = System.getenv("APPDATA") + File.separator + "road";
		File dirFile = new File(parentDir);
		dirFile.mkdirs();
		try {
			resourceCopy.copyResourcesToDir(dirFile, false,
					Constants.RESOURCE_PATH + "levels/track_1.map",
					Constants.RESOURCE_PATH + "levels/track_2.map",
					Constants.RESOURCE_PATH + "levels/track_3.map");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Open Window
		Window window = new Window(instance, "R.O.A.D.");
		RendererSettings settings = window.getSettings();
		
		settings.setBackgroundColor(2, 148, 0);
		Image image = new Image("icon", ImageLocation.JAR.getImage(Constants.RESOURCE_PATH + "car_yellow.png"));
		settings.setWindowIcon(image);
		settings.setMaximizedByDefault(true);
		settings.setFPSCap(0);
		
		window.open();
	}
	
	private static RoadProject instance;
	private Menu menu;
	
	private Config settingsConfig;
	
	private boolean changedMenu;
	
	private double second;
	private int fps;
	private int frames;
	
	private long lastFrame;
	
	public RoadProject() {
		instance = this;
	}
	
	public static RoadProject get() {
		return instance;
	}
	
	public static void loadSettings() {
		Config config = Config.getConfig("settings.txt", ResourceLocation.FILE, "settings.txt", ResourceLocation.JAR);
		if (config != null) {
			get().settingsConfig = config;
			String name = config.get("name");
			if (name != null) Constants.PLAYER_NAME = name;
			String quality = config.get("quality");
			if (quality != null) {
				RenderQuality renderQuality = RenderQuality.valueOf(quality.toUpperCase());
				if (renderQuality != null) {
					Constants.RENDER_QUALITY = renderQuality;
				} else {
					System.out.println("Unknown quality: " + quality);
				}
			}
			String showNames = config.get("showNames");
			if (showNames != null) {
				Constants.SHOW_NAMES = Boolean.valueOf(showNames);
			}
			String showFrames = config.get("showFrames");
			if (showFrames != null) {
				Constants.SHOW_FRAMES = Boolean.valueOf(showFrames);
			}
			String limitFrames = config.get("limitFrames");
			if (limitFrames != null) {
				Constants.LIMIT_FRAMES = Boolean.valueOf(limitFrames);
			}
			String ip = config.get("ip");
			if (ip != null) {
				Constants.SERVER_IP = ip;
			}
			String port = config.get("port");
			if (port != null) {
				Constants.SERVER_PORT = Integer.valueOf(port);
			}
			String arrowControls = config.get("arrowControls");
			if (arrowControls != null) {
				Constants.ARROW_CONTROLS = Boolean.valueOf(arrowControls);
			}
		} else {
			System.err.println("Couldn't load settings.txt");
		}
	}
	
	@Override
	public void onLoad(Context context) {
		setMenu(new MainMenu());
		Constants.CONTROLS = new Controls(context.getKeyboard());
	}
	
	@Override
	public void render(Graph g, double deltaTimeMS) {
		changedMenu = false;
		
		long currentTime = System.currentTimeMillis();
		if (Constants.SHOW_FRAMES) {
			g.addDebugString("currentTimeMillis: " + currentTime);
			second -= deltaTimeMS;
			if (second < 0) {
				second += 1000;
				fps = frames;
				frames = 0;
			}
			frames++;
			currentTime = System.currentTimeMillis();
			g.addDebugString("Frame Time: " + (currentTime - lastFrame));
			lastFrame = currentTime;
		}
		
		menu.update(deltaTimeMS / 1000);
		
		if (Constants.SHOW_FRAMES) {
			g.addDebugString("Update Time: " + (System.currentTimeMillis() - currentTime));
			currentTime = System.currentTimeMillis();
		}
		
		long renderTime = System.currentTimeMillis();
		if (changedMenu) {
		} else {
			menu.render(g);
		}
		renderTime = System.currentTimeMillis() - renderTime;
		
		if (Constants.SHOW_FRAMES) {
			g.addDebugString("Draw Time: " + (System.currentTimeMillis() - currentTime));
			currentTime = System.currentTimeMillis();
			
			g.getHUD().drawText("FPS: " + fps, g.getHUD().getWidth() - 5, 30, Graph.getColor(255, 255, 255), Constants.MAIN_MENU_FONT, TextAlignment.RIGHT);
			
			g.getHUD().drawRect(0, 0, 300, 500, Color.WHITE);
			TimeLogger.getKeys().forEach(key -> {
				g.addDebugString("" + key + " Time: " + TimeLogger.getTime(key));
			});
		}
		if (Constants.LIMIT_FRAMES) {
			long fps = 120;
			long sleepTime = 1000 / fps - renderTime;
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setMenu(Menu menu) {
		changedMenu = true;
		if (this.menu != null) {
			this.menu.onChange();
		}
		this.menu = menu;
		menu.setContext(getContext());
		menu.onLoad(getContext());
	}
	
	public Config getSettingsConfig() {
		return settingsConfig;
	}
}
