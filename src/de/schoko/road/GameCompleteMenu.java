package de.schoko.road;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.TextAlignment;
import de.schoko.road.game.Car;
import de.schoko.road.game.CoreGame;
import de.schoko.road.game.PlayerCar;

public class GameCompleteMenu extends Menu {
	private List<Car> carList;
	private Car[] cars;
	private CoreGame game;
	private TextButton mainMenuButton;
	private long startTimeStamp;
	private PlayerCar playerCar;
	
	public GameCompleteMenu(CoreGame coreGame, long startTimeStamp) {
		this.playerCar = coreGame.getPlayerCar();
		this.startTimeStamp = startTimeStamp;
		this.game = coreGame;
		carList = game.getCars();
	}
	
	@Override
	public void onLoad(Context context) {
		context.getSettings().setBackgroundColor(Constants.MAIN_MENU_BACKGROUND_COLOR);
		
		mainMenuButton = new TextButton(context, "Back to main menu", 0, 0, Color.RED, Constants.MAIN_MENU_FONT, Constants.MAIN_MENU_BACKGROUND_COLOR, Constants.MAIN_MENU_BACKGROUND_COLOR, Color.WHITE, Color.LIGHT_GRAY);
		
		this.carList.sort((o1, o2) -> {
			if (o1.getCompleteTime() > o2.getCompleteTime()) return 1;
			if (o1.getCompleteTime() < o2.getCompleteTime()) return -1;
			return 0;
		});
		cars = this.carList.toArray(new Car[0]);
	}
	
	@Override
	public void update(double deltaTime) {
		if (mainMenuButton.wasReleased()) {
			RoadProject.get().setMenu(new MainMenu());
		}
	}

	@Override
	public void render(Graph g) {
		HUDGraph hud = g.getHUD();
		int baseY = 300;
		Font font = Constants.MAIN_MENU_FONT;
		int fontSize = font.getSize();
		for (int i = 0; i < cars.length; i++) {
			Car car = cars[i];
			long delta = car.getCompleteTime() - startTimeStamp;
			int y = baseY + i * fontSize;
			hud.drawText("" + (i + 1) + ". " + car.getName(), hud.getWidth() / 2 - 250, y, car.getColor(), font);
			hud.drawText("" + convert(delta), hud.getWidth() / 2 + 250, y, car.getColor(), font, TextAlignment.RIGHT);
		}
		int place = (this.carList.indexOf(playerCar) + 1);
		hud.drawText("> ", hud.getWidth() / 2 - 250, baseY + (place - 1) * fontSize, Color.WHITE, font, TextAlignment.RIGHT);
		
		hud.drawText("You reached " + place + ". place!", hud.getWidth() / 2, 150, Color.WHITE, font, TextAlignment.CENTER);
		hud.drawText("Your time is " + convert(playerCar.getCompleteTime() - startTimeStamp), hud.getWidth() / 2, 150 + fontSize, Color.WHITE, font, TextAlignment.CENTER);
		
		mainMenuButton.setX((int) (hud.getWidth() / 2), TextAlignment.CENTER);
		mainMenuButton.setY((int) (hud.getHeight() - mainMenuButton.getHeight() - 8));
		hud.draw(mainMenuButton);
	}
	
	public static String convert(long timeMS) {
		long millisceonds = timeMS % 1000;
		long timeS = timeMS / 1000;
		long seconds = timeS % 60;
		long timeM = timeS / 60;
		long minutes = timeM;
		
		return "" + minutes + ":" + fixPlaces(seconds, 2) + "." + fixPlaces(millisceonds, 3);
	}

	public static String fixPlaces(double val, int places) {
		 return String.format("%0" + places + ".0f", val);
	}
}
