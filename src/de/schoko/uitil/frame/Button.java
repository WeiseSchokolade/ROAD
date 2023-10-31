package de.schoko.uitil.frame;

import de.schoko.rendering.Context;
import de.schoko.rendering.HUDGraph;

public abstract class Button {
	private Context context;
	private int x, y;
	private int width, height;
	
	public Button(int height) {
		this.height = height;
	}
	
	public abstract void draw(HUDGraph hud);
	
	public void update(double deltaTimeMS) {
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Context getContext() {
		return context;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
