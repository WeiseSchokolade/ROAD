package de.schoko.uitil.frame;

import java.awt.Color;

import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.Mouse;
import de.schoko.rendering.TextAlignment;

public class EventButton extends Button {
	public static final Color DEFAULT_COLOR = Graph.getColor(255, 255, 255);
	public static final Color HIGHLIGHTED_COLOR = Graph.getColor(200, 200, 200);
	public static final Color PRESSED_COLOR = Graph.getColor(170, 170, 170);
	
	private static final int MAX_WIDTH = 200;
	private String buttonText;
	private InteractionListener interactionListener;
	private boolean currentlyPressed;
	private boolean currentlyHighlighted;
	private int suggestedWidth;
	
	public EventButton(String buttonText, InteractionListener interactionListener) {
		super(30);
		this.buttonText = buttonText;
		this.interactionListener = interactionListener;
	}

	@Override
	public void draw(HUDGraph hud) {
		Color color = DEFAULT_COLOR;
		if (currentlyPressed) {
			color = PRESSED_COLOR;
		} else if (currentlyHighlighted) {
			color = HIGHLIGHTED_COLOR;
		}
		hud.drawRect(getX(), getY(), getWidth(), getHeight(), color);
		hud.drawText(buttonText, getX() + getWidth() / 2 , getY() + 20, Graph.getColor(0, 0, 0), Frame.NAME_FONT, TextAlignment.CENTER);
	}
	
	@Override
	public void update(double deltaTimeMS) {
		Mouse mouse = getContext().getMouse();
		if (mouse.getScreenX() > getX() && mouse.getScreenY() > getY() &&
				mouse.getScreenX() < getX() + getWidth() && mouse.getScreenY() < getY() + getHeight()) {
			currentlyHighlighted = true;
			if (mouse.isPressed(Mouse.LEFT_BUTTON)) {
				currentlyPressed = true;
			} else {
				if (currentlyPressed) {
					interactionListener.call();
					currentlyPressed = false;
				}
			}
		} else {
			currentlyHighlighted = false;
			currentlyPressed = false;
		}
	}
	
	@Override
	public void setWidth(int width) {
		super.setWidth(Math.min(width, MAX_WIDTH));
		this.suggestedWidth = width;
	}
	
	@Override
	public void setX(int x) {
		super.setX(x + (suggestedWidth - getWidth()) / 2);
	}
}
