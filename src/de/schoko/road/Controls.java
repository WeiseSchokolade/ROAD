package de.schoko.road;

import de.schoko.rendering.Keyboard;

public class Controls {
	private boolean turnLeft, turnRight;
	private boolean driveForwards, driveBackwards;
	
	private Keyboard keyboard;
	
	private int turnLeftKey = Keyboard.A;
	private int turnRightKey = Keyboard.D;
	private int driveForwardsKey = Keyboard.W;
	private int driveBackwardsKey = Keyboard.S;
	
	public Controls(Keyboard keyboard) {
		this.keyboard = keyboard;
	}
	
	public void update() {
		turnLeft = keyboard.isPressed(turnLeftKey);
		turnRight = keyboard.isPressed(turnRightKey);
		driveForwards = keyboard.isPressed(driveForwardsKey);
		driveBackwards = keyboard.isPressed(driveBackwardsKey);
	}
	
	public boolean isTurnLeft() {
		return turnLeft;
	}
	
	public boolean isTurnRight() {
		return turnRight;
	}
	
	public boolean isDriveForwards() {
		return driveForwards;
	}
	
	public boolean isDriveBackwards() {
		return driveBackwards;
	}
	
	public void setTurnLeftKey(int turnLeftKey) {
		this.turnLeftKey = turnLeftKey;
	}
	
	public void setTurnRightKey(int turnRightKey) {
		this.turnRightKey = turnRightKey;
	}
	
	public void setDriveForwardsKey(int driveForwardsKey) {
		this.driveForwardsKey = driveForwardsKey;
	}
	
	public void setDriveBackwardsKey(int driveBackwardsKey) {
		this.driveBackwardsKey = driveBackwardsKey;
	}
}
