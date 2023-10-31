package de.schoko.road.layers;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;

public abstract class Layer {
	private Context context;
	
	public void onLoad(Context context) {
		
	}
	
	public final void setContext(Context context) {
		onLoad(context);
		this.context = context;
	}
	
	public abstract void update(double deltaTime);
	public abstract void draw(Graph g);
	
	public Context getContext() {
		return context;
	}
}
