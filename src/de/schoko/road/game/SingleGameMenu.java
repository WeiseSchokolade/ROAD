package de.schoko.road.game;

import java.util.List;

import de.schoko.rendering.Camera;
import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.Image;
import de.schoko.rendering.ImageLocation;
import de.schoko.road.Constants;
import de.schoko.road.GameCompleteMenu;
import de.schoko.road.Map;
import de.schoko.road.RoadProject;
import de.schoko.road.layers.WeatherLayer;

public class SingleGameMenu extends GameMenu {
	private Car playerCar;
	private boolean carCamera = true;
	private boolean carCompleted = false;
	private double camX, camY;
	private int unfinishedCarAmount;
	
	private boolean started;
	private double startTime;
	private long startTimestamp;
	private double lightY;
	private double lightImageHeight;
	private boolean lightHidden;

	private Image[] lights;
	
	public SingleGameMenu(Map map) {
		super(map);
	}

	@Override
	public void onLoad(Context context, CoreGame coreGame) {
		if (Constants.RAINING) {
			coreGame.addLayer(new WeatherLayer());
		}
		context.getCamera().setZoomable(false);
		context.getCamera().setMovable(false);

		playerCar = new PlayerCar(context, coreGame.getRoadLayer());
		coreGame.addCar(playerCar);
		for (int i = 0; i < 8; i++) {
			coreGame.addCar(new EnemyCar(context, coreGame.getRoadLayer()));
		}
		
		lights = new Image[5];
		lights[0] = context.getImagePool().getImage("light_0", Constants.RESOURCE_PATH + "light_0.png", ImageLocation.JAR);
		lights[1] = context.getImagePool().getImage("light_1", Constants.RESOURCE_PATH + "light_1.png", ImageLocation.JAR);
		lights[2] = context.getImagePool().getImage("light_2", Constants.RESOURCE_PATH + "light_2.png", ImageLocation.JAR);
		lights[3] = context.getImagePool().getImage("light_3", Constants.RESOURCE_PATH + "light_3.png", ImageLocation.JAR);
		lights[4] = context.getImagePool().getImage("light_4", Constants.RESOURCE_PATH + "light_4.png", ImageLocation.JAR);
		lightImageHeight = lights[0].getHeight() / 0.3;
	}

	@Override
	public void update(double deltaTime) {
		Camera camera = getContext().getCamera();
		
		if (playerCar.hasCompleted()) {
			if (carCompleted) {
				camera.setX(camX);
				camera.setY(camY);
				
				List<Car> cars = getCoreGame().getCars();
				unfinishedCarAmount = cars.size();
				for (int i = 0; i < cars.size(); i++) {
					if (cars.get(i).hasCompleted()) {
						unfinishedCarAmount--;
					}
				}
				if (unfinishedCarAmount < 1) {
					RoadProject.get().setMenu(new GameCompleteMenu(getCoreGame(), startTimestamp));
					return;
				}
			} else {
				carCamera = false;
				carCompleted = true;
				camX = camera.getX();
				camY = camera.getY();
			}
		}
		
		if (started) {
			getCoreGame().update(deltaTime);
			if (!lightHidden) {
				lightY -= 150 * deltaTime;
				if (lightY < -lightImageHeight) {
					lightHidden = true;
				}
			}
			if (carCamera) {
				camera.setX(playerCar.getPos().getX());
				camera.setY(playerCar.getPos().getY());
				camera.setZoom(150);
			}
		} else {
			camera.setX(playerCar.getPos().getX());
			camera.setY(playerCar.getPos().getY());
			camera.setZoom(Math.min(50 + 200 * (startTime / 4), 150));
			startTime += deltaTime;
			if (startTime > 6) {
				startTime = 6;
				started = true;
				startTimestamp = System.currentTimeMillis();
			}
		}
	}
	
	@Override
	public void render(Graph g) {
		HUDGraph hud = g.getHUD();
		getCoreGame().draw(g);

		if (!lightHidden) {
			hud.drawImage((hud.getWidth() - (lights[0].getWidth() / 0.3)) / 2, lightY, lights[(int) Math.max(Math.min(startTime - 2, 4), 0)], 0.3);
		}
	}
	
	public Car getPlayerCar() {
		return playerCar;
	}
	
	public long getStartTimestamp() {
		return startTimestamp;
	}
}
