package de.schoko.road;

import de.schoko.rendering.Context;
import de.schoko.rendering.Image;
import de.schoko.rendering.ImageLocation;

public enum CarModel {
	BLUE("Blue", "car"),
	RED("Red", "car_red"),
	YELLOW("Yellow", "car_yellow");

	private String name;
	private String imageName;

	CarModel(String name, String imageName) {
		this.name = name;
		this.imageName = imageName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getImageName() {
		return imageName;
	}
	
	public Image getImage(Context context) {
		return context.getImagePool().getImage(imageName, Constants.RESOURCE_PATH + imageName + ".png", ImageLocation.JAR);
	}
}
