package de.schoko.road.server.shared.packets;

import de.schoko.road.game.Car;
import de.schoko.road.geometry.Vector2D;
import de.schoko.road.server.GameConnection;

public class CarInfoPacket extends Packet {
	public Vector2D pos;
	public Vector2D dir;
	public double speed;
	public double rotationSpeed;
	public Vector2D speedVec;
	public double acceleration;
	public int round;
	public double progress;
	public boolean completed;
	public long completeTime;
	public String carImageName;
	
	public CarInfoPacket() {
		
	}
	
	public CarInfoPacket(
			Vector2D pos,
			Vector2D dir,
			double speed,
			double rotationSpeed,
			Vector2D speedVec,
			double acceleration,
			int round,
			double progress,
			boolean completed,
			long completeTime,
			String carImageName) {
		this.pos = pos;
		this.dir = dir;
		this.speed = speed;
		this.rotationSpeed = rotationSpeed;
		this.speedVec = speedVec;
		this.acceleration = acceleration;
		this.round = round;
		this.progress = progress;
		this.completed = completed;
		this.completeTime = completeTime;
		this.carImageName = carImageName;
	}
	
	public CarInfoPacket(Car car) {
		pos = car.getPos();
		dir = car.getDir();
		speed = car.getSpeed();
		rotationSpeed = car.getRotationSpeed();
		speedVec = car.getSpeedVec();
		acceleration = car.getAcceleration();
		round = car.getRound();
		progress = car.getProgress();
		completed = car.hasCompleted();
		completeTime = car.getCompleteTime();
		carImageName = car.getImageName();
	}
	
	public CarInfoPacket(GameConnection connection) {
		pos = connection.getPos();
		dir = connection.getDir();
		speed = connection.getSpeed();
		rotationSpeed = connection.getRotationSpeed();
		speedVec = connection.getSpeedVec();
		acceleration = connection.getAcceleration();
		round = connection.getRound();
		progress = connection.getProgress();
		completed = connection.hasCompleted();
		completeTime = connection.getCompleteTime();
		carImageName = connection.getCarImageName();
	}
	
	public void apply(Car car) {
		car.setPos(pos);
		car.setDir(dir);
		car.setSpeed(speed);
		car.setRotationSpeed(rotationSpeed);
		car.setSpeedVec(speedVec);
		car.setAcceleration(acceleration);
		car.setRound(round);
		car.setProgress(progress);
		car.setCompleted(completed);
		car.setCompleteTime(completeTime);
		car.setImage(carImageName);
	}
}
