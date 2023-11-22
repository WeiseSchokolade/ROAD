package de.schoko.road.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.Image;
import de.schoko.rendering.ImageLocation;
import de.schoko.rendering.Keyboard;
import de.schoko.road.Constants;
import de.schoko.road.Map;
import de.schoko.road.geometry.Collider;
import de.schoko.road.geometry.Vector2D;
import de.schoko.road.layers.Layer;
import de.schoko.road.layers.RoadLayer;
import de.schoko.road.server.shared.SharedConstants;
import de.schoko.utility.TimeLogger;

public class CoreGame {
	private Context context;
	private Map map;

	private Image goal;
	private RoadLayer roadLayer;
	private PlayerCar playerCar;
	
	private UpdaterHandler updaterHandler;
	
	private List<Car> cars;
	private List<Collider> colliders;
	private List<Layer> layers;
	private List<GameObject> objects;
	
	public CoreGame(Map map) {
		this.map = map;
		cars = new ArrayList<>();
		colliders = new ArrayList<>();
		layers = new ArrayList<>();
		objects = new ArrayList<>();
		roadLayer = new RoadLayer(map);
		updaterHandler = new UpdaterHandler();
	}
	
	public void onLoad(Context context) {
		this.context = context;
		context.getSettings().setBackgroundColor(2, 148, 0);
		
		goal = context.getImagePool().getImage("finish", Constants.RESOURCE_PATH + "finish.png", ImageLocation.JAR);
		
		if (Constants.ARROW_CONTROLS) {
			Constants.CONTROLS.setTurnLeftKey(Keyboard.LEFT);
			Constants.CONTROLS.setTurnRightKey(Keyboard.RIGHT);
			Constants.CONTROLS.setDriveForwardsKey(Keyboard.UP);
			Constants.CONTROLS.setDriveBackwardsKey(Keyboard.DOWN);
		}

		addLayer(roadLayer);
	}
	
	public void update(double deltaTime) {
		TimeLogger.start("CoreGameUpdate");
		Constants.CONTROLS.update();
		layers.forEach(layer -> {
			layer.update(deltaTime);
		});
		colliders.forEach((collider) -> {
			collider.updateSpeed(deltaTime);
		});
		colliders.forEach((collider) -> {
			collider.update(deltaTime);
		});
		updaterHandler.update(deltaTime);
		for (int i = 0; i < cars.size(); i++) {
			Car car = cars.get(i);
			if (car.hasCompleted() && !(car instanceof PlayerCar)) continue;
			car.update(deltaTime);
		}
		TimeLogger.end("CoreGameUpdate");
	}
	
	public void draw(Graph g) {
		TimeLogger.start("Core Draw Time");
		HUDGraph hud = g.getHUD();

		TimeLogger.start("Core Layer Time");
		layers.forEach(layer -> {
			layer.draw(g);
		});
		TimeLogger.end("Core Layer Time");

		g.drawImage(goal, map.getEntry().getX(), map.getEntry().getY(), 2.9005);

		for (int i = 0; i < cars.size(); i++) {
			Car car = cars.get(i);
			car.draw(g);
			if (car.hasCompleted()) continue;
			roadLayer.minimapDrawPoint(g, car.getPos(), car.getColor());
		}

		for (int i = 0; i < cars.size(); i++) {
			Car car = cars.get(i);
			double stringWidth = Graph.getStringWidth(car.getName(), Constants.GAME_LEADERBOARD_FONT);
			
			hud.drawText("" + car.getName(), 5, 20 + i * 25, car.getColor(), Constants.GAME_LEADERBOARD_FONT);
			hud.drawText(" - " + car.getRound(), 5 + stringWidth, 20 + i * 25, Color.WHITE, Constants.GAME_LEADERBOARD_FONT);
		}
		TimeLogger.end("Core Draw Time");
	}

	public void addCar(Car car) {
		addCar(car, cars.size());
	}
	
	public void addCar(Car car, int id) {
		if (car instanceof PlayerCar) this.playerCar = (PlayerCar) car;
		
		Vector2D entry = map.getEntry();
		Vector2D dir = map.getEntryDirection();
		Vector2D offDir = dir.rotate(Math.toRadians(90));
		Vector2D pos = entry.copy();
		
		car.setColor(SharedConstants.COLOR_ORDER[id]);
		car.setDir(dir);
		
		double dirMultiplier = id / 3 + 1;
		double offDirMultiplier = -1 + (id % 3);
		pos = pos.subtract(dir.multiply(dirMultiplier)).subtract(offDir.multiply(offDirMultiplier));
		
		car.setPos(pos);
		
		this.cars.add(car);
		car.onLoad();
	}
	
	public void addLayer(Layer layer) {
		this.layers.add(layer);
		layer.setContext(context);
	}
	
	public List<GameObject> getObjects() {
		return objects;
	}
	
	public List<Layer> getLayers() {
		return layers;
	}
	
	public RoadLayer getRoadLayer() {
		return roadLayer;
	}
	
	public List<Car> getCars() {
		return cars;
	}
	
	public PlayerCar getPlayerCar() {
		return playerCar;
	}
}
