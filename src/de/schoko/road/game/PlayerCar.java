package de.schoko.road.game;

import de.schoko.rendering.Context;
import de.schoko.rendering.Keyboard;
import de.schoko.road.Constants;
import de.schoko.road.layers.RoadLayer;

public class PlayerCar extends Car {
	private Context context;
	private double maxRotationSpeed = 120;
	
	public PlayerCar(Context context, RoadLayer roadLayer) {
		super(Constants.PLAYER_NAME, context, roadLayer, Constants.CAR_MODEL, 10, 20);
		this.context = context;
	}
	
	@Override
	public void updateControls(double deltaTime) {
		Keyboard keyboard = context.getKeyboard();
		
		if (hasCompleted()) {
			rotationSpeed = 0;
			return;
		}
		
		if (keyboard.isPressed(Keyboard.A, Keyboard.LEFT)) {
			rotationSpeed = maxRotationSpeed;
		} else if (keyboard.isPressed(Keyboard.D, Keyboard.RIGHT)) {
			rotationSpeed = -maxRotationSpeed;
		} else {
			rotationSpeed = 0;
		}
		
		if (keyboard.isPressed(Keyboard.W, Keyboard.UP)) {
			acceleration = maxAcceleration;
		} else if (keyboard.isPressed(Keyboard.S, Keyboard.DOWN)) {
			acceleration = -maxAcceleration;
		} else {
			acceleration = 0;
		}
	}
}
