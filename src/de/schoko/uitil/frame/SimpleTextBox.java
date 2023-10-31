package de.schoko.uitil.frame;

import java.awt.Color;

import de.schoko.rendering.HUDGraph;

public class SimpleTextBox extends Button {
	private String text;
	
	public SimpleTextBox(String text) {
		super(30);
		this.text = text;
	}

	@Override
	public void draw(HUDGraph hud) {
		hud.drawText(text, getX(), getY() + 20, Color.WHITE, Frame.NAME_FONT);
	}
	
	public void setText(String text) {
		this.text = text;
	}
}
