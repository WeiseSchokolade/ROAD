package de.schoko.road.game;

import de.schoko.rendering.Graph;

public abstract class Entity {

	public abstract void update(double deltaTime);
	public abstract void draw(Graph g);
	
}
