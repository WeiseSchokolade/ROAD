package de.schoko.road;

import java.awt.Color;
import java.awt.Graphics2D;

import de.schoko.rendering.Context;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.Image;
import de.schoko.rendering.Mouse;
import de.schoko.rendering.TextAlignment;
import de.schoko.rendering.hud.DrawCall;

public class CarSelectionButton extends DrawCall {
	private Context context;
	private TextButton textButton;
	private CarModel model;
	private int carIndex;
	private CarModel[] models;
	private boolean mousePressed;
	
	public CarSelectionButton(Context context) {
		this.context = context;
		model = Constants.CAR_MODEL;
		models = CarModel.values();
		for (int i = 0; i < models.length; i++) {
			if (models[i].equals(model)) {
				carIndex = i;
				break;
			}
		}
		textButton = new TextButton(context, "Car: " + model.getName(), 0, 0, Color.RED, Constants.MAIN_MENU_FONT,
				Constants.MAIN_MENU_BACKGROUND_COLOR, Constants.MAIN_MENU_BACKGROUND_COLOR, Color.WHITE, Color.LIGHT_GRAY);
	}

	@Override
	public void call(Graphics2D g2D) {
		HUDGraph hud = context.getLastGraph().getHUD();
		Mouse mouse = context.getMouse();
		
		int axis = ((int) (hud.getWidth() * 0.75));
		textButton.setX(axis, TextAlignment.CENTER);
		textButton.setY((int) (hud.getHeight() / 2));

		Image carImage = model.getImage(context);
		double carImageX = axis - carImage.getWidth() * 0.5 * 8;
		double carImageY = textButton.getY() + textButton.getHeight();
		if (textButton.wasReleased()) {
			nextCar();
		}
		if (mouse.isPressed(Mouse.LEFT_BUTTON)) {
			mousePressed = true;
		} else {
			if (mousePressed) {
				mousePressed = false;
				if (mouse.getScreenX() > carImageX && mouse.getScreenY() > carImageY) {
					if (mouse.getScreenX() < carImageX + carImage.getWidth() * 8 && mouse.getScreenY() < carImageY + carImage.getHeight() * 8) {
						nextCar();
					}
				}
			}
		}
		
		hud.draw(textButton);

		hud.drawImage(carImageX, carImageY, carImage, 0.125);
	}
	
	public void nextCar() {
		carIndex++;
		if (carIndex >= models.length) {
			carIndex = 0;
		}
		Constants.CAR_MODEL = models[carIndex];
		model = models[carIndex];
		textButton.setText("Car: " + model.getName());
	}
}
