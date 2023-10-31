package de.schoko.road.game;

import de.schoko.rendering.Context;
import de.schoko.road.CarModel;
import de.schoko.road.layers.RoadLayer;

public class RemoteCar extends Car {

	public RemoteCar(String name, Context context, RoadLayer roadLayer) {
		super(name, context, roadLayer, CarModel.BLUE, 10, 20);
	}

	@Override
	public void updateControls(double deltaTime) {
		
	}
	
	@Override
	protected double calculateProgress() {
		return getProgress();
	}
}
