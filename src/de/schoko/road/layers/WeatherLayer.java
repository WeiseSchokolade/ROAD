package de.schoko.road.layers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.hud.DrawCall;

public class WeatherLayer extends Layer {
	private Raindrop[] raindrops;
	private double distributionTime;
	
	@Override
	public void onLoad(Context context) {
		raindrops = new Raindrop[100];
		
		for (int i = 0; i < raindrops.length; i++) {
			raindrops[i] = new Raindrop();
			raindrops[i].redistrubite(context.getLastGraph().getHUD().getWidth(), context.getLastGraph().getHUD().getHeight());
		}
	}
	
	@Override
	public void update(double deltaTime) {
		distributionTime -= deltaTime;
		double width = getContext().getLastGraph().getHUD().getWidth();
		double height = getContext().getLastGraph().getHUD().getHeight();
		if (distributionTime < 0) {
			int index = (int) (Math.random() * raindrops.length);
			raindrops[index].redistrubite(width, height);
			distributionTime += 0.01;
		}
		
		for (int i = 0; i < raindrops.length; i++) {
			raindrops[i].update(deltaTime, width, height);
		}
	}

	@Override
	public void draw(Graph g) {
		HUDGraph hud = g.getHUD();

		hud.draw(new DrawCall() {
			@Override
			public void call(Graphics2D g2d) {
				g2d.setStroke(new BasicStroke(3f));
			}
		});
		double camX = g.convSX(0);
		double camY = g.convSY(0);
		for (int i = 0; i < raindrops.length; i++) {
			raindrops[i].draw(hud, camX, camY);
		}
		
		hud.drawRect(0, 0, hud.getWidth(), hud.getHeight(), Graph.getColor(50, 50, 50, 180));
	}
	
	class Raindrop {
		private double x, y;
		public void redistrubite(double width, double height) {
			x = Math.random() * (width - 250) + 250;
			y = Math.random() * (height - 250) - 250;
		}
		
		public void update(double deltaTime, double width, double height) {
			x -= deltaTime * 750;
			y += deltaTime * 750;
			if (x < 0) {
				x += width;
			}
			if (y > height) {
				y -= height;
			}
		}
		
		public void draw(HUDGraph hud, double camX, double camY) {
			double drawnX = (x + camX) % hud.getWidth();
			double drawnY = (y + camY) % hud.getHeight();
			hud.drawLine(drawnX, drawnY, drawnX - 5, drawnY + 5, Color.BLUE);
		}
	}
}
