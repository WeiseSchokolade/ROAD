package de.schoko.road.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import de.schoko.rendering.Camera;
import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.Image;
import de.schoko.rendering.ImageLocation;
import de.schoko.rendering.Keyboard;
import de.schoko.road.Constants;
import de.schoko.road.GameComplete;
import de.schoko.road.Map;
import de.schoko.road.Menu;
import de.schoko.road.RoadProject;
import de.schoko.road.Vector2D;
import de.schoko.road.layers.RoadLayer;

public class SingleGame extends Menu {
	private Map map;
	private boolean single;
	
	private RoadLayer roadLayer;

	private ArrayList<Car> cars;
	
	private Car playerCar;
	private boolean carCamera = true;
	private boolean carCompleted = false;
	private double camX, camY;
	
	private boolean started;
	private double startTime;
	private long startTimestamp;
	private double lightY;
	private double lightImageHeight;
	private boolean lightHidden;
	
	private Image goal;
	private Image[] lights;
	private int unfinishedCarAmount;

	public SingleGame(Map map, boolean single) {
		this.map = map;
		cars = new ArrayList<>();
		this.single = single;
		roadLayer = new RoadLayer(map.getCatmullRomSpline());
	}
	
	@Override
	public void onLoad(Context context) {
		context.getSettings().setBackgroundColor(2, 148, 0);

		roadLayer.setContext(getContext());
		
		lights = new Image[5];
		lights[0] = context.getImagePool().getImage("light_0", Constants.RESOURCE_PATH + "light_0.png", ImageLocation.JAR);
		lights[1] = context.getImagePool().getImage("light_1", Constants.RESOURCE_PATH + "light_1.png", ImageLocation.JAR);
		lights[2] = context.getImagePool().getImage("light_2", Constants.RESOURCE_PATH + "light_2.png", ImageLocation.JAR);
		lights[3] = context.getImagePool().getImage("light_3", Constants.RESOURCE_PATH + "light_3.png", ImageLocation.JAR);
		lights[4] = context.getImagePool().getImage("light_4", Constants.RESOURCE_PATH + "light_4.png", ImageLocation.JAR);
		goal = context.getImagePool().getImage("finish", Constants.RESOURCE_PATH + "finish.png", ImageLocation.JAR);
		lightImageHeight = lights[0].getHeight() / 0.3;
		
		if (single) {
			playerCar = new PlayerCar(context, roadLayer);
			addCar(playerCar);
			for (int i = 0; i < 8; i++) {
				addCar(new EnemyCar(context, roadLayer));
			}
		}
	}
	
	@Override
	public void update(double deltaTime) {
		Camera camera = getContext().getCamera();
		Keyboard keyboard = getContext().getKeyboard();
		
		if (Constants.DEV_ACCESS) {
			if (keyboard.wasRecentlyPressed(Keyboard.F1)) {
				Constants.DRAW_ROAD = !Constants.DRAW_ROAD;
			}
			if (keyboard.wasRecentlyPressed(Keyboard.F2)) {
				carCamera = !carCamera;
			}
		}
		if (playerCar.hasCompleted()) {
			if (carCompleted) {
				camera.setX(camX);
				camera.setY(camY);
				
				unfinishedCarAmount = cars.size();
				for (int i = 0; i < cars.size(); i++) {
					if (cars.get(i).hasCompleted()) {
						unfinishedCarAmount--;
					}
				}
				if (unfinishedCarAmount < 1) {
					RoadProject.get().setMenu(new GameComplete(this));
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
			for (int i = 0; i < cars.size(); i++) {
				Car car = cars.get(i);
				if (car.hasCompleted() && !(car instanceof PlayerCar)) continue;
				car.update(deltaTime);
			}
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
	public void render(Graph ge) {
		ge.addDebugString("UnfinishedCarAmount: " + unfinishedCarAmount);
		Graphics2D graphics = ge.getAWTGraphics();
		//double turnAngle = Math.toRadians(playerCar.getDir().getAngle() - 90);
		//graphics.rotate(turnAngle, ge.getViewport().getWidth() / 2, ge.getViewport().getHeight() / 2);
		Graph g = new Graph(getContext().getWindow().getPanel(), graphics, getContext().getCamera(), getContext().getSettings(), getContext().getSettings().getGraphTransform(), getContext().getPanelSystem());
		HUDGraph hud = ge.getHUD();
		
		roadLayer.draw(g);
		
		g.drawImage(goal, map.getEntry().getX(), map.getEntry().getY(), 2.9005);
		
		for (int i = 0; i < cars.size(); i++) {
			Car car = cars.get(i);
			car.draw(g);
			if (car.hasCompleted()) continue;
			roadLayer.minimapDrawPoint(g, car.getPos(), car.getColor());
		}
		playerCar.draw(g);
		
		if (!lightHidden) {
			hud.drawImage((hud.getWidth() - (lights[0].getWidth() / 0.3)) / 2, lightY, lights[(int) Math.max(Math.min(startTime - 2, 4), 0)], 0.3);
		}
		
		for (int i = 0; i < cars.size(); i++) {
			Car car = cars.get(i);
			double stringWidth = Graph.getStringWidth(car.getName(), Constants.GAME_LEADERBOARD_FONT);
			
			hud.drawText("" + car.getName(), 5, 20 + i * 25, car.getColor(), Constants.GAME_LEADERBOARD_FONT);
			hud.drawText(" - " + car.getRound(), 5 + stringWidth, 20 + i * 25, Color.WHITE, Constants.GAME_LEADERBOARD_FONT);
		}
		
		//graphics.rotate(-turnAngle, ge.getViewport().getWidth() / 2, ge.getViewport().getHeight() / 2);
		g.finalize();
	}
	
	public void addCar(Car car) {
		addCar(car, cars.size());
	}
	
	public void addCar(Car car, int id) {
		Vector2D entry = map.getEntry();
		Vector2D dir = map.getEntryDirection();
		Vector2D offDir = dir.rotate(Math.toRadians(90));
		Vector2D pos = entry.copy();
		
		car.setColor(Constants.COLOR_ORDER[id]);
		car.setDir(dir);
		
		double dirMultiplier = id / 3 + 1;
		double offDirMultiplier = -1 + (id % 3);
		pos = pos.subtract(dir.multiply(dirMultiplier)).subtract(offDir.multiply(offDirMultiplier));
		
		car.setPos(pos);
		
		this.cars.add(car);
		car.onLoad();
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public void setPlayerCar(Car playerCar) {
		this.playerCar = playerCar;
	}
	
	public Car getPlayerCar() {
		return playerCar;
	}
	
	public ArrayList<Car> getCars() {
		return cars;
	}

	public RoadLayer getRoadLayer() {
		return roadLayer;
	}
	
	public long getStartTimestamp() {
		return startTimestamp;
	}
}
