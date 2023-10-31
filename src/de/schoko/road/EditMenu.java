package de.schoko.road;

import java.awt.Color;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.ImageLocation;
import de.schoko.rendering.Keyboard;
import de.schoko.rendering.Mouse;
import de.schoko.rendering.shapes.ImageFrame;
import de.schoko.road.layers.RoadLayer;

public class EditMenu extends Menu {
	private static final double[] STRAIGHT_D = { 1, 0 };
	private static final double[] LEFT_D = { 1, 0.5 };
	private static final double[] RIGHT_D = { 1, -0.5 };

	private Map map;
	
	private RoadLayer roadLayer;
	private CatmullRomSpline catmullRomSpline;
	private Vector2D lastPoint;
	private Vector2D lastDir;
	
	private ImageFrame trackImage;

	public EditMenu(Map map) {
		this.map = map;
		catmullRomSpline = this.map.getCatmullRomSpline();
		lastPoint = new Vector2D();
		lastDir = new Vector2D(1, 0);
		catmullRomSpline.addPoint(new Vector2D(0, -2));
		catmullRomSpline.addPoint(new Vector2D(0, -1));
		catmullRomSpline.addPoint(new Vector2D(0, 0));
		catmullRomSpline.addPoint(new Vector2D(0, 1));
		catmullRomSpline.addPoint(new Vector2D(0, 2));
		catmullRomSpline.addPoint(new Vector2D(0, 3));
		lastPoint = catmullRomSpline.getPoints().get(catmullRomSpline.getPoints().size() - 1);
	}
	
	@Override
	public void onLoad(Context context) {
		context.getSettings().setBackgroundColor(2, 148, 0);
		context.getCamera().setCameraPath(null);
		
		Constants.DRAW_ROAD = false;
		
		roadLayer = new RoadLayer(catmullRomSpline);
		roadLayer.setContext(context);
		
		trackImage = new ImageFrame(-12.75, 4, context.getImagePool().getImage("track-1", "de/schoko/road/track 1.png", ImageLocation.JAR), 22.5);
	}
	
	@Override
	public void update(double deltaTime) {
		Keyboard keyboard = getContext().getKeyboard();
		Mouse mouse = getContext().getMouse();
		
		if (keyboard.wasRecentlyPressed(Keyboard.F1)) {
			Constants.DRAW_ROAD = !Constants.DRAW_ROAD;
		}
		if (keyboard.wasRecentlyPressed(Keyboard.F12)) {
			Maps.saveMap(map);
		}
		
		if (keyboard.wasRecentlyPressed(Keyboard.ONE) || keyboard.wasRecentlyPressed(Keyboard.TWO)
				|| keyboard.wasRecentlyPressed(Keyboard.THREE)) {

			double[] pattern = null;

			if (keyboard.wasRecentlyPressed(Keyboard.ONE)) {
				pattern = LEFT_D;
			} else if (keyboard.wasRecentlyPressed(Keyboard.TWO)) {
				pattern = STRAIGHT_D;
			} else if (keyboard.wasRecentlyPressed(Keyboard.THREE)) {
				pattern = RIGHT_D;
			}

			Vector2D[] controlPoints = getPoints(lastPoint, lastDir, pattern);
			
			catmullRomSpline.addPoint(controlPoints[1]);
			lastPoint = controlPoints[1];
			lastDir = lastDir.rotate(Math.toRadians(45 * pattern[1]));
		}
		if (keyboard.wasRecentlyPressed(Keyboard.BACK_SPACE)) {
			catmullRomSpline.getPoints().remove(catmullRomSpline.getPoints().size() - 1);
			lastDir = catmullRomSpline.getDerivative(catmullRomSpline.getMaxT() - 1);
			lastPoint = catmullRomSpline.getPoints().get(catmullRomSpline.getPoints().size() - 1);
		}
		if (mouse.wasRecentlyPressed(Mouse.LEFT_BUTTON)) {
			Vector2D point = new Vector2D(mouse.getX(), mouse.getY());
			catmullRomSpline.addPoint(point);
			lastDir = point.subtract(lastPoint).normalize();
			lastPoint = point;
		}
	}
	
	@Override
	public void render(Graph g) {
		g.draw(trackImage);

		roadLayer.draw(g);
		
		g.fillCircle(lastPoint.getX(), lastPoint.getY(), Color.CYAN, 0.25);
		g.drawLine(lastPoint.getX(), lastPoint.getY(), lastDir.getX() + lastPoint.getX(), lastDir.getY() + lastPoint.getY(), Color.BLUE, 0.05f);
	}
	
	public Vector2D[] getPoints(Vector2D sourcePoint, Vector2D dir, double[] pattern) {
		Vector2D normDir = dir.normalize();
		Vector2D offDir = normDir.rotate(Math.toRadians(90));

		Vector2D[] points = new Vector2D[4];
		points[0] = sourcePoint;
		points[1] = sourcePoint.add(normDir.multiply(pattern[0])).add(offDir.multiply(pattern[1]));

		return points;
	}
}
