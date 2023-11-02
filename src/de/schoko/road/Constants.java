package de.schoko.road;

import java.awt.Color;
import java.awt.Font;

import de.schoko.rendering.Graph;

public class Constants {
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
	
	public static RenderQuality RENDER_QUALITY = RenderQuality.NORMAL;
	
	public static boolean SHOW_NAMES = true;
	public static boolean DRAW_ROAD = true;
	public static boolean SHOW_FRAMES = false;
	
	public static double ROAD_WIDTH = 1.5;
	public static double ROAD_BOUNDS_WIDTH = 1.4;
	public static double ROAD_AI_WIDTH = 0.8;
	
	public static Color MINIMAP_COLOR = Graph.getColor(255, 255, 255, 127);
}
