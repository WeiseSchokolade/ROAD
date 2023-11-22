package de.schoko.road.game;

import de.schoko.rendering.Graph;

public abstract class GameObject {
	private boolean removed;
	
	public abstract void update(double deltaTime);
	public abstract void draw(Graph g);
	
	public boolean isRemoved() {
		return removed;
	}
	
	public void remove() {
		removed = true;
	}
}
