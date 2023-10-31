package de.schoko.road;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;

public abstract class Menu {
	private Context context;
	public void onLoad(Context context) {
		
	}
	public void onChange() {
		
	}
	public abstract void update(double deltaTime);
	public abstract void render(Graph g);
	
	public final Context getContext() {
		return context;
	}
	public final void setContext(Context context) {
		this.context = context;
	}
}
