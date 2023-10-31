package de.schoko.road.game;

import java.awt.Color;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.Image;
import de.schoko.rendering.ImageLocation;
import de.schoko.rendering.TextAlignment;
import de.schoko.road.CarModel;
import de.schoko.road.Constants;
import de.schoko.road.Vector2D;
import de.schoko.road.layers.RoadLayer;

public abstract class Car extends Entity {
	private Context context;
	private RoadLayer roadLayer;
	
	private String name;
	private Color color;
	private Color fadedColor;
	private Vector2D pos;
	protected double maxAcceleration;
	protected double acceleration;
	private double maxSpeed;
	private Vector2D speedVec;
	private double speed;
	
	protected double rotationSpeed;
	private Vector2D dir;
	
	private int round;
	private boolean completed;
	private long completeTime;
	
	private double nearestT;
	private double progress;
	private boolean checkpoint05Reached;
	
	private String imageName;
	private Image image;
	
	public Car(String name, Context context, RoadLayer roadLayer, CarModel carModel, double maxSpeed, double maxAcceleration) {
		this.context = context;
		this.name = name;
		this.roadLayer = roadLayer;
		this.maxSpeed = maxSpeed;
		this.maxAcceleration = maxAcceleration;
		setImage(carModel.getImageName());
		
		pos = new Vector2D();
		dir = new Vector2D(0, 1);
		speedVec = new Vector2D();
	}
	
	/** Optionally overwritable method called after car is added to game*/
	public void onLoad() {
		
	}
	
	public abstract void updateControls(double deltaTime);
	
	@Override
	public void update(double deltaTime) {
		speed *= 1 - deltaTime * 2;
		speedVec = speedVec.multiply(1 - deltaTime * 2);
		
		updateControls(deltaTime);
		dir = dir.rotate(Math.toRadians(rotationSpeed * deltaTime));
		speed = Math.min(Math.max(speed + acceleration * deltaTime, -maxSpeed), maxSpeed);
		
		speedVec = speedVec.add(dir.multiply(speed * deltaTime));
		Vector2D newPos = pos.add(speedVec.multiply(deltaTime));
		if (roadLayer.inBounds(newPos)) {
			pos = newPos;
		} else {
			speedVec = new Vector2D();
			speed = 0;
		}
		progress = calculateProgress();
		if (progress > 0.45 && progress < 0.55) {
			checkpoint05Reached = true;
		}
		if (progress == 0.0 && checkpoint05Reached) {
			round++;
			checkpoint05Reached = false;
			if (round > 2 && !completed) {
				completed = true;
				completeTime = System.currentTimeMillis();
			}
		}
	}

	@Override
	public void draw(Graph g) {
		if (completed && (!(this instanceof PlayerCar))) {
			return;
		}
		// g.fillCircle(pos.getX(), pos.getY(), fadedColor, 0.5);
		g.drawRotatedImage(image, pos.getX(), pos.getY(), 32, -dir.getAngle());
		if (Constants.SHOW_NAMES) {
			g.drawString(name, pos.getX(), pos.getY() + 0.5, fadedColor, Constants.GAME_LEADERBOARD_FONT, TextAlignment.CENTER);
		}
	}
	
	protected double calculateProgress() {
		nearestT = roadLayer.getNearestT(pos);
		progress = nearestT / roadLayer.getCatmullRomSpline().getMaxT();
		return progress;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getRound() {
		return round;
	}
	
	public Vector2D getPos() {
		return pos;
	}
	
	public Vector2D getDir() {
		return dir;
	}
	
	public double getNearestT() {
		return nearestT;
	}
	
	public void setPos(Vector2D pos) {
		this.pos = pos;
	}
	
	public void setDir(Vector2D dir) {
		this.dir = dir;
	}
	
	public void setColor(Color color) {
		this.color = color;
		fadedColor = Graph.getColor(color.getRed(), color.getGreen(), color.getBlue(), 127);
	}
	
	public void setImage(String imageName) {
		if (this.imageName == null || !this.imageName.equals(imageName)) {
			this.imageName = imageName;
			this.image = context.getImagePool().getImage(imageName, "de/schoko/road/" + imageName + ".png", ImageLocation.JAR);
		}
	}

	public double getSpeed() {
		return speed;
	}

	public double getRotationSpeed() {
		return rotationSpeed;
	}
	
	public Vector2D getSpeedVec() {
		return speedVec;
	}

	public double getAcceleration() {
		return acceleration;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void setRotationSpeed(double rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	public void setSpeedVec(Vector2D speedVec) {
		this.speedVec = speedVec;
	}
	
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}
	
	public double getProgress() {
		return progress;
	}
	
	public void setProgress(double progress) {
		this.progress = progress;
	}
	
	public void setRound(int round) {
		this.round = round;
	}

	public String getImageName() {
		return imageName;
	}
	
	public boolean hasCompleted() {
		return completed;
	}
	
	public long getCompleteTime() {
		return completeTime;
	}
	
	public boolean isCheckpoint05Reached() {
		return checkpoint05Reached;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public void setCompleteTime(long completeTime) {
		this.completeTime = completeTime;
	}
}
