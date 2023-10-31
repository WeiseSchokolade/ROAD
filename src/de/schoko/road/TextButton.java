package de.schoko.road;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.Mouse;
import de.schoko.rendering.TextAlignment;
import de.schoko.rendering.hud.DrawCall;

public class TextButton extends DrawCall {
	private Mouse mouse;
	private String text;
	private int x, y;
	private int width, height;
	private Color textColor;
	private Font font;
	private Color backgroundColor;
	private Color outlineColor;
	private Color pressedOutlineColor;
	private Color highlightedOutlineColor;
	private int horizontalMargin = 5;
	private int verticalMargin = 5;
	
	private boolean hovered;
	private boolean pressed;
	private boolean wasRecentlyPressed;
	private boolean released;
	
	public TextButton(Context context, String text, int x, int y, Color textColor, Font font, Color backgroundColor, Color outlineColor, Color pressedOutlineColor, Color highlightedOutlineColor) {
		this.mouse = context.getMouse();
		this.x = x;
		this.y = y;
		this.textColor = textColor;
		this.backgroundColor = backgroundColor;
		this.outlineColor = outlineColor;
		this.pressedOutlineColor = pressedOutlineColor;
		this.highlightedOutlineColor = highlightedOutlineColor;
		setFont(font);
		setText(text);
	}
	
	@Override
	public void call(Graphics2D g2D) {
		// Update
		hovered = (mouse.getScreenX() >= x && mouse.getScreenX() < x + width &&
			mouse.getScreenY() >= y && mouse.getScreenY() < y + height);
		
		released = false;
		if (hovered) {
			if (mouse.isPressed(Mouse.LEFT_BUTTON)) {
				pressed = true;
			} else {
				if (wasRecentlyPressed) {
					released = true;
				}
				pressed = false;
			}
		} else {
			pressed = false;
		}
		wasRecentlyPressed = pressed;
		
		// Draw
		g2D.setColor(backgroundColor);
		g2D.fillRect(x, y, width, height);
		g2D.setColor(textColor);
		
		if (pressed) {
			g2D.setColor(pressedOutlineColor);
		} else if (hovered) {
			g2D.setColor(highlightedOutlineColor);
		} else {
			g2D.setColor(outlineColor);
		}
		
		g2D.drawRect(x, y, width, height);
		g2D.setFont(font);
		g2D.setColor(textColor);
		g2D.drawString(text, x + horizontalMargin, (int) (y + height * 0.75));
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setX(int x, TextAlignment alignment) {
		if (alignment == TextAlignment.LEFT) {
			this.x = x;
		} else if (alignment == TextAlignment.CENTER) {
			this.x = x - width / 2;
		} else if (alignment == TextAlignment.RIGHT) {
			this.x = x - width;
		}
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setText(String text) {
		this.text = text;
		this.width = Graph.getStringWidth(text, font) + horizontalMargin * 2;
	}
	
	public void setFont(Font font) {
		this.font = font;
		this.height = font.getSize() + verticalMargin * 2;
	}
	
	public boolean isHovered() {
		return hovered;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	public boolean wasReleased() {
		return released;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
