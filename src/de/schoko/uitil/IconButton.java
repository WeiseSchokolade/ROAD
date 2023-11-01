package de.schoko.uitil;

import java.awt.Graphics2D;
import java.awt.Image;

import de.schoko.rendering.Graph;
import de.schoko.rendering.Mouse;
import de.schoko.rendering.hud.DrawCall;

public class IconButton extends DrawCall {
	private Image image;
	private Mouse mouse;
	private int x, y;
	private int width, height;
	private boolean mousePressed;
	private boolean mouseReleased;
	private boolean enabled;
	
	public IconButton(de.schoko.rendering.Image image, Mouse mouse, double x, double y, double scale) {
		this.image = image.getBufferedImage();
		this.mouse = mouse;
		this.x = (int) x;
		this.y = (int) y;
		this.width = (int) (this.image.getWidth(null) * scale);
		this.height = (int) (this.image.getHeight(null) * scale);
		this.image = this.image.getScaledInstance(width, height, Image.SCALE_FAST);
		this.enabled = true;
	}
	
	@Override
	public void call(Graphics2D g2D) {
		if (isHovered() && enabled) {
			g2D.setColor(Graph.getColor(200, 200, 200));
			g2D.fillRect(x - 1, y - 1, width + 2, height + 2);
		}
		g2D.drawImage(image, x, y, null);
		boolean mousePressed = isPressed();
		if (this.mousePressed && !mousePressed) {
			this.mouseReleased = true;
		} else {
			this.mouseReleased = false;
		}
		this.mousePressed = mousePressed;
	}
	
	public boolean isPressed() {
		if (isHovered() && enabled) {
			return mouse.isPressed(Mouse.LEFT_BUTTON);
		}
		return false;
	}
	
	public boolean wasReleased() {
		if (mouseReleased) {
			mouseReleased = false;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isHovered() {
		return (mouse.getScreenX() > x && mouse.getScreenY() > y &&
				mouse.getScreenX() < x + width && mouse.getScreenY() < y + height);
	}
	
	public void setX(double x) {
		this.x = (int) x;
	}
	
	public void setY(double y) {
		this.y = (int) y;
	}
	
	public void setXY(double x, double y) {
		this.x = (int) x;
		this.y = (int) y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
}
