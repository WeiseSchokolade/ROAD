package de.schoko.road;

import java.util.ArrayList;

public class CatmullRomSpline {
	private static final double ACCURACY = 0.01;
	private ArrayList<Vector2D> points;
	private double length;
	
	public CatmullRomSpline() {
		points = new ArrayList<>();
	}
	
	public Vector2D getInternalPoint(double t, int pointsIndex) {
		Vector2D p0 = points.get(pointsIndex);
		Vector2D p1 = points.get(pointsIndex + 1);
		Vector2D p2 = points.get(pointsIndex + 2);
		Vector2D p3 = points.get(pointsIndex + 3);
		
		return (					p1.multiply(2)				.add(
				p0.multiply(-1)									.add(p2)									.multiply(t))		.add(
				p0.multiply(2)		.subtract(p1.multiply(5))	.add(p2.multiply(4))		.subtract(p3)	.multiply(t * t)).add(
				p0.multiply(-1)		.add(p1.multiply(3))		.subtract(p2.multiply(3))	.add(p3)		.multiply(t * t * t))).multiply(0.5);
	}
	
	public Vector2D getPoint(double t) {
		return getInternalPoint(t % 1, (int) Math.floor(t));
	}
	
	public Vector2D getInternalDerivative(double t, int pointsIndex) {
		Vector2D p0 = points.get(pointsIndex);
		Vector2D p1 = points.get(pointsIndex + 1);
		Vector2D p2 = points.get(pointsIndex + 2);
		Vector2D p3 = points.get(pointsIndex + 3);
		
		return (p0.multiply(-1)									.add(p2)									.multiply(1))		.add(
				p0.multiply(2)		.subtract(p1.multiply(5))	.add(p2.multiply(4))		.subtract(p3)	.multiply(2 * t * t)).add(
				p0.multiply(-1)		.add(p1.multiply(3))		.subtract(p2.multiply(3))	.add(p3)		.multiply(3 * t * t * t)).multiply(0.5);
	}

	public Vector2D getDerivative(double t) {
		return getInternalDerivative(t % 1, (int) Math.floor(t));
	}
	
	public void addPoint(Vector2D v) {
		points.add(v);
		if (getMaxT() > 0) {
			recalculateLength();
		}
	}
	
	public ArrayList<Vector2D> getPoints() {
		return points;
	}
	
	public int getPointAmount() {
		return points.size();
	}
	
	public void recalculateLength() {
		length = 0;
		Vector2D lastPoint = getPoint(0);
		for (double t = ACCURACY; t < getMaxT(); t += ACCURACY) {
			Vector2D point = getPoint(t);
			length += lastPoint.distance(point);
			lastPoint = point;
		}
	}
	
	// Returns the max t usable. Higher values will lead to exceptions 
	public int getMaxT() {
		return points.size() - 3;
	}
	
	public double getLength() {
		return length;
	}
}
