package de.schoko.road.game;

import de.schoko.rendering.Context;
import de.schoko.road.Constants;
import de.schoko.road.Controls;
import de.schoko.road.layers.RoadLayer;

public class PlayerCar extends Car {
	private Controls controls;
	private double maxRotationSpeed = 120;
	
	public PlayerCar(Context context, RoadLayer roadLayer) {
		super(Constants.PLAYER_NAME, context, roadLayer, Constants.CAR_MODEL, 10, 20);
		this.controls = Constants.CONTROLS;
	}
	
	@Override
	public void updateControls(double deltaTime) {
		if (hasCompleted()) {
			rotationSpeed = 0;
			return;
		}
		
		if (controls.isTurnLeft()) {
			rotationSpeed = maxRotationSpeed;
		} else if (controls.isTurnRight()) {
			rotationSpeed = -maxRotationSpeed;
		} else {
			rotationSpeed = 0;
		}
		
		if (controls.isDriveForwards()) {
			acceleration = maxAcceleration;
		} else if (controls.isDriveBackwards()) {
			acceleration = -maxAcceleration;
		} else {
			acceleration = 0;
		}
	}
}
