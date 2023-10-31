package de.schoko.road;

import java.awt.Color;
import java.awt.Font;

import de.schoko.rendering.Graph;

public class Constants {
	public static final int VERSION = 1;
	public static boolean DEV_ACCESS = false;
	public static String RESOURCE_PATH = "src/de/schoko/road/";

	public static Font MAIN_MENU_FONT = new Font("Verdana", Font.PLAIN, 30);
	public static Font MAIN_MENU_FONT_BIG = new Font("Verdana", Font.PLAIN, 60);
	public static Font MAIN_MENU_SMALL_FONT = new Font("Verdana", Font.PLAIN, 15);
	public static Font GAME_LEADERBOARD_FONT = new Font("Verdana", Font.PLAIN, 20);
	public static Color MAIN_MENU_BACKGROUND_COLOR = Graph.getColor(59, 59, 59);
	
	public static String SERVER_IP = "127.0.0.1";
	public static int SERVER_PORT = 5555;
	
	public static String PLAYER_NAME = "Player";
	public static CarModel CAR_MODEL = CarModel.BLUE;
	
	public static RenderQuality renderQuality = RenderQuality.NORMAL;
	
	public static boolean SHOW_NAMES = true;
	public static boolean DRAW_ROAD = true;
	public static boolean SHOW_FRAMES = false;
	
	public static double ROAD_WIDTH = 1.5;
	public static double ROAD_BOUNDS_WIDTH = 1.4;
	public static double ROAD_AI_WIDTH = 0.6;
	
	public static Color MINIMAP_COLOR = Graph.getColor(255, 255, 255, 127);
	public static Color[] COLOR_ORDER = {
			Graph.getColor(0, 0, 255),
			Graph.getColor(255, 0, 0),
			Graph.getColor(0, 255, 0),
			Graph.getColor(255, 255, 0),
			Graph.getColor(0, 255, 255),
			Graph.getColor(255, 0, 255),
			Graph.getColor(255, 127, 0),
			Graph.getColor(0, 255, 127),
			Graph.getColor(127, 0, 255)
	};
}
