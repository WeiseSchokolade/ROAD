package de.schoko.road.layers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.Image;
import de.schoko.road.Constants;
import de.schoko.road.Map;
import de.schoko.road.RenderQuality;
import de.schoko.road.geometry.CatmullRomSpline;
import de.schoko.road.geometry.Vector2D;
import de.schoko.utility.Logging;

public class RoadLayer extends Layer {
	private CatmullRomSpline catmullRomSpline;
	private Image image;
	private Image mapImage;
	private Map map;
	
	public RoadLayer(Map map) {
		this.map = map;
		this.catmullRomSpline = map.getCatmullRomSpline();
	}
	
	@Override
	public void onLoad(Context context) {
		if (map.getMiniMapImage() != null) {
			image = new Image("miniMap", map.getMiniMapImage());
		} else {
			BufferedImage bufferedImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2D = bufferedImage.createGraphics();
			renderMinimap(g2D, 512, 512);
			g2D.dispose();
			map.setMiniMapImage(bufferedImage);
			image = new Image("miniMap", bufferedImage);
		}
		if (map.getMapImage() != null) {
			mapImage = new Image("map", map.getMapImage());
		} else {
			BufferedImage bufferedImage = new BufferedImage(16384, 16384, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2D = bufferedImage.createGraphics();
			RenderQuality oldQuality = Constants.RENDER_QUALITY;
			Constants.RENDER_QUALITY = RenderQuality.HIGH;
			renderMap(g2D, 16384, 16384);
			Constants.RENDER_QUALITY = oldQuality;
			g2D.dispose();
			map.setMapImage(bufferedImage);
			mapImage = new Image("map", bufferedImage);
		}
	}
	
	@Override
	public void update(double deltaTime) {
		
	}

	@Override
	public void draw(Graph g) {
		HUDGraph hud = g.getHUD();
		hud.drawImage(hud.getWidth() - image.getWidth(), hud.getHeight() - image.getHeight(), image, 1);
		g.drawImage(mapImage, 0, 0, 150);
	}
	
	public void renderMinimap(Graphics2D g2D, int width, int height) {
		Stroke prevStroke = g2D.getStroke();
		g2D.setStroke(new BasicStroke(10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		g2D.setColor(Graph.getColor(255, 255, 255));
		
		Vector2D point = null;
		Vector2D lastPoint = null;
		for (double t = 0; t < catmullRomSpline.getMaxT(); t += Constants.RENDER_QUALITY.getRoadDetail()) {
			point = catmullRomSpline.getPoint(t);
			
			if (lastPoint != null) {
				minimapDrawLine(g2D, width, height, lastPoint, point);
			}
			lastPoint = point;
		}
		g2D.setStroke(prevStroke);
	}

	public void minimapDrawLine(Graphics2D g2D, int width, int height, Vector2D v0, Vector2D v1) {
		g2D.drawLine(		(int) (width - 80 + v0.getX() * 4),
							(int) (height - 80 - v0.getY() * 4),
							(int) (width - 80 + v1.getX() * 4),
							(int) (height - 80 - v1.getY() * 4));
	}
	
	public void renderMap(Graphics2D g2D, int width, int height) {
		int halfWidth = width / 2;
		int halfHeight = height / 2;
		if (Constants.DRAW_ROAD) {
			if (Constants.RENDER_QUALITY.hasSmoothEdges()) {
				g2D.setStroke(new BasicStroke(35f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
			} else {
				g2D.setStroke(new BasicStroke(35f));
			}
			
			g2D.setColor(Color.GRAY);
			Vector2D[] points = new Vector2D[18];
			Vector2D point;
			for (double t = 0; t < catmullRomSpline.getMaxT(); t += Constants.RENDER_QUALITY.getRoadDetail()) {
				point = catmullRomSpline.getPoint(t);
				Vector2D derivative = catmullRomSpline.getDerivative(t).normalize().multiply(Constants.ROAD_WIDTH);
				Vector2D offDeriv = derivative.rotate(Math.toRadians(90));
				Vector2D[] newPoints = new Vector2D[points.length];
				for (int j = 0; j < newPoints.length; j++) {
					newPoints[j] = point.add(offDeriv.multiply(-1 + 2 * (((double) j) / newPoints.length)));
				}
				if (points[0] != null) {
					for (int j = 0; j < newPoints.length; j++) {
						drawLine(g2D, halfWidth, halfHeight, newPoints[j], points[j]);
					}
				}
				points = newPoints;
			}
			g2D.setColor(Color.WHITE);
			Vector2D lastLeftLine = null;
			Vector2D lastRightLine = null;
			for (double t = 0; t < catmullRomSpline.getMaxT(); t += Constants.RENDER_QUALITY.getRoadDetail()) {
				point = catmullRomSpline.getPoint(t);
				
				Vector2D derivative = catmullRomSpline.getDerivative(t).normalize().multiply(Constants.ROAD_WIDTH);
				Vector2D offDeriv = derivative.rotate(Math.toRadians(90));
				Vector2D leftLine = point.add(offDeriv);
				Vector2D rightLine = point.add(offDeriv.multiply(-1));
				
				if (lastLeftLine != null) {
					drawLine(g2D, halfWidth, halfHeight, leftLine, lastLeftLine);
					drawLine(g2D, halfWidth, halfHeight, rightLine, lastRightLine);
				}
				lastLeftLine = leftLine;
				lastRightLine = rightLine;
			}
		} else {
			g2D.setColor(Color.YELLOW);
			for (double t = 0; t < catmullRomSpline.getMaxT(); t += 0.1) {
				Vector2D point = catmullRomSpline.getPoint(t);
				g2D.drawArc((int) (point.getX() * 150), (int) (point.getY() * 150), 30, 30, 0, 360);
			}
			if (Constants.RENDER_QUALITY.hasSmoothEdges()) {
				g2D.setStroke(new BasicStroke(15f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			}
			for (int i = 0; i < catmullRomSpline.getPointAmount() - 3; i++) {
				Vector2D point = catmullRomSpline.getPoints().get(i);
				Vector2D derivative = catmullRomSpline.getDerivative(i).normalize();
				Vector2D offDerivative = derivative.rotate(Math.toRadians(90));
				drawPoint(g2D, halfWidth, halfHeight, point, Color.GREEN);
				drawLine(g2D, halfWidth, halfHeight, point, derivative.add(point), Color.RED);
				drawLine(g2D, halfWidth, halfHeight, point, offDerivative.add(point), Color.GREEN);
			}
		}
	}
	
	public void drawPoint(Graphics2D g2D, int halfWidth, int halfHeight, Vector2D v, Color color) {
		g2D.setColor(color);
		g2D.drawArc((int) (halfWidth + v.getX() * 150), (int) (halfHeight - v.getY() * 150), 30, 30, 0, 360);
	}

	public void drawLine(Graphics2D g2D, int halfWidth, int halfHeight, Vector2D v0, Vector2D v1) {
		g2D.drawLine((int) (halfWidth + v0.getX() * 150), (int) (halfHeight - v0.getY() * 150), (int) (halfWidth + v1.getX() * 150), (int) (halfHeight - v1.getY() * 150));
	}

	public void drawLine(Graphics2D g2D, int halfWidth, int halfHeight, Vector2D v0, Vector2D v1, Color color) {
		g2D.setColor(color);
		g2D.drawLine((int) (halfWidth + v0.getX() * 150), (int) (halfHeight - v0.getY() * 150), (int) (halfWidth + v1.getX() * 150), (int) (halfHeight - v1.getY() * 150));
	}

	public boolean inBounds(Vector2D pos) {
		return getNearestT(pos) != -1;
	}
	
	/**
	 * Returns the nearest t or -1 when the pos is out of bounds
	 */
	public double getNearestT(Vector2D pos) {
		for (int i = 0; i < catmullRomSpline.getMaxT(); i++) {
			//if (pos.subtract(catmullRomSpline.getPoints().get(i)).getLengthSQ() > 100) continue;
			for (double t = 0; t < 1; t += 0.1) {
				Vector2D v = catmullRomSpline.getInternalPoint(t, i);
				if (pos.subtract(v).getLengthSQ() < Constants.ROAD_BOUNDS_WIDTH * Constants.ROAD_BOUNDS_WIDTH) {
					return i + t;
				}
			}
			
		}
		return -1;
	}

	public void minimapDrawPoint(Graph g, Vector2D v, Color color) {
		HUDGraph hud = g.getHUD();
		hud.drawCircle(hud.getWidth() - 80 + v.getX() * 4,
					 hud.getHeight() - 80 - v.getY() * 4,
					 10, color);
	}
	
	public CatmullRomSpline getCatmullRomSpline() {
		return catmullRomSpline;
	}
}