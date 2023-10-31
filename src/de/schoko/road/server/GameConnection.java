package de.schoko.road.server;

import de.schoko.road.Vector2D;
import de.schoko.road.server.packets.Packet;
import de.schoko.serverbase.core.Connection;

public class GameConnection {
	private Connection connection;
	
	private Vector2D pos;
	private Vector2D dir;
	private double speed;
	private double rotationSpeed;
	private Vector2D speedVec;
	private double acceleration;
	private int round;
	private double progress;
	private boolean completed;
	private long completeTime;
	private String carImageName;
	
	private int id;
	
	private int version;
	private String name;
	private String map;

	public GameConnection(Connection connection, int id) {
		this.connection = connection;
		this.id = id;
		
		pos = new Vector2D();
		dir = new Vector2D(0, 1);
		speed = 0;
		rotationSpeed = 0;
		speedVec = new Vector2D();
		carImageName = "car";
	}
	
	public void send(String s) {
		connection.send(s);
	}
	
	public void send(Packet packet) {
		connection.send(Packet.toJson(packet));
	}
	
	public String read() {
		return connection.read();
	}
	
	public void close() {
		connection.close();
	}
	
	public boolean isClosed() {
		return connection.isClosed();
	}
	
	public int getVersion() {
		return version;
	}
	
	public int getId() {
		return id;
	}
	
	public Vector2D getPos() {
		return pos;
	}
	
	public Vector2D getDir() {
		return dir;
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
	
	public int getRound() {
		return round;
	}
	
	public double getProgress() {
		return progress;
	}
	
	public boolean hasCompleted() {
		return completed;
	}
	
	public long getCompleteTime() {
		return completeTime;
	}
	
	public String getName() {
		return name;
	}
	
	public String getMap() {
		return map;
	}
	
	public String getCarImageName() {
		return carImageName;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setMap(String map) {
		this.map = map;
	}
	
	public void setPos(Vector2D pos) {
		this.pos = pos;
	}
	
	public void setDir(Vector2D dir) {
		this.dir = dir;
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
	
	public void setRound(int round) {
		this.round = round;
	}
	
	public void setProgress(double progress) {
		this.progress = progress;
	}
	
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public void setCompleteTime(long completeTime) {
		this.completeTime = completeTime;
	}
	
	public void setCarImageName(String carImageName) {
		this.carImageName = carImageName;
	}
	
	public Connection getConnection() {
		return connection;
	}
}
