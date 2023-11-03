package de.schoko.road.geometry;

public class Vector2D {
	private double x;
	private double y;
	
	public Vector2D() {
	}
	
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(double x, double y, Vector2D xAxis, Vector2D yAxis) {
		Vector2D t = xAxis.multiply(x).add(yAxis.multiply(y));
		this.x = t.x;
		this.y = t.y;
	}
	
	public Vector2D copy() {
		return new Vector2D(x, y);
	}
	
	public Vector2D add(double val) {
		Vector2D copy = this.copy();
		copy.x += val;
		copy.y += val;
		return copy;
	}
	
	public Vector2D add(Vector2D v) {
		Vector2D copy = this.copy();
		copy.x += v.x;
		copy.y += v.y;
		return copy;
	}
	
	public Vector2D subtract(double val) {
		Vector2D copy = this.copy();
		copy.x -= val;
		copy.y -= val;
		return copy;
	}
	
	public Vector2D subtract(Vector2D v) {
		Vector2D copy = this.copy();
		copy.x -= v.x;
		copy.y -= v.y;
		return copy;
	}
	
	public Vector2D multiply(double val) {
		Vector2D copy = this.copy();
		copy.x *= val;
		copy.y *= val;
		return copy;
	}
	
	public Vector2D multiply(Vector2D v) {
		Vector2D copy = this.copy();
		copy.x *= v.x;
		copy.y *= v.y;
		return copy;
	}
	
	/**
	 * Implementation of lerp function <br>
	 * f(x) = (1 - t) * p0 + t * p1
	 */
	public Vector2D lerp(double t, Vector2D v) {
		return copy().multiply(1 - t).add(v.multiply(t));
	}
	
	/**
	 * Get distance using Math.sqrt();
	 */
	public double distance(Vector2D v) {
		double a = x - v.x;
		double b = y - v.y;
		return Math.sqrt(a * a + b * b);
	}
	
	public Vector2D rotate(double radians) {
		Vector2D a = new Vector2D(Math.cos(radians), Math.sin(radians));
		Vector2D b = new Vector2D(-Math.sin(radians), Math.cos(radians));
		return a.multiply(this.x).add(b.multiply(this.y));
	}
	
	public Vector2D normalize() {
		Vector2D copy = copy();
		double length = copy.getLength();
		copy.x /= length;
		copy.y /= length;
		return copy;
	}
	
	public double dotProduct(Vector2D v) {
		double dotProduct = v.x * x + v.y * y;
		return dotProduct;
	}
	
	/**
	 * @return The angle to the x-axis, in radians
	 */
	public double angle() {
		return Math.atan2(y, x);
	}
	
	public double angle(Vector2D v) {
		double dotProduct = this.dotProduct(v);
		return Math.acos(dotProduct / (v.getLength() * getLength()));
	}
	
	public double angle2(Vector2D v) {
		return Math.atan2(v.y, v.x) - Math.atan2(y, x);
	}
	
	public double getLength() {
		return Math.sqrt(getLengthSQ());
	}
	
	public double getLengthSQ() {
		return x * x + y * y;
	}
	
	/**
	 * @return The angle of the Vector in degrees
	 */
	public double getAngle() {
		Vector2D copy = this.normalize();
		return (Math.toDegrees(Math.atan2(copy.y, copy.x)) + 360) % 360;
	}
	
	@Override
	public String toString() {
		return "(" + x + "|" + y + ")";
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
}
