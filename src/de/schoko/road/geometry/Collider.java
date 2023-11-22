package de.schoko.road.geometry;

import de.schoko.rendering.Graph;

public abstract class Collider {
	public abstract void update(double deltaTime);
	public abstract void updateSpeed(double deltaTime);
	public abstract void draw(Graph g);
}
