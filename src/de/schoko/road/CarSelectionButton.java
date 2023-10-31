package de.schoko.road;

import java.awt.Color;
import java.awt.Graphics2D;

import de.schoko.rendering.Context;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.Image;
import de.schoko.rendering.TextAlignment;
import de.schoko.rendering.hud.DrawCall;

public class CarSelectionButton extends DrawCall {
	private Context context;
	private TextButton textButton;
	private CarModel model;
	private int carIndex;
	private CarModel[] models;
	
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
		
		if (textButton.wasReleased()) {
			carIndex++;
			if (carIndex >= models.length) {
				carIndex = 0;
			}
			Constants.CAR_MODEL = models[carIndex];
			model = models[carIndex];
			textButton.setText("Car: " + model.getName());
		}
		
		textButton.setX((int) ((2 * hud.getWidth()) / 3), TextAlignment.LEFT);
		textButton.setY((int) (hud.getHeight() / 2));
		hud.draw(textButton);

		Image image = model.getImage(context);
		hud.drawImage(textButton.getX(), textButton.getY() + textButton.getHeight(), image, 0.25);
	}
}
