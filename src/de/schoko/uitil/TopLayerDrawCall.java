package de.schoko.uitil;

import java.awt.Graphics2D;

import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.hud.DrawCall;

public class TopLayerDrawCall extends DrawCall {
	private HUDGraph hud;
	private DrawCall drawCall;
	
	public TopLayerDrawCall(HUDGraph hud, DrawCall drawCall) {
		this.hud = hud;
		this.drawCall = drawCall;
	}
	
	@Override
	public void call(Graphics2D g2d) {
		hud.draw(drawCall);
	}
}
