package de.schoko.road.game;

import de.schoko.rendering.Context;
import de.schoko.road.CarModel;
import de.schoko.road.CatmullRomSpline;
import de.schoko.road.Constants;
import de.schoko.road.Vector2D;
import de.schoko.road.layers.RoadLayer;

public class EnemyCar extends Car {
	private static final double DEFAULT_T = 5;
	
	private CatmullRomSpline spline;
	private double currentT = DEFAULT_T;
	private Vector2D target;
	private double targetOffset;
	private double targetOffsetChange;
	private double targetChangeTime;

	public EnemyCar(Context context, RoadLayer roadLayer) {
		super("Enemy", context, roadLayer, CarModel.BLUE, 9 + Math.random() * 2, 20);
		spline = roadLayer.getCatmullRomSpline();
		//targetOffset = Math.random() * Constants.ROAD_AI_WIDTH * 2 - Constants.ROAD_AI_WIDTH;
		
		//target = new Vector2D(targetOffset, 0);
	}
	
	@Override
	public void onLoad() {
		Vector2D splinePos = spline.getPoint(currentT);
		Vector2D splineDir = spline.getDerivative(currentT);
		
		Vector2D startDir = splineDir.normalize();
		Vector2D spawnDir = splinePos.subtract(getPos());
		Vector2D splineNormal = splineDir.rotate(Math.toRadians(90));
		
		double dotProduct = startDir.dotProduct(spawnDir);
		Vector2D move = startDir.multiply(-dotProduct);
		Vector2D offsetVec = spawnDir.add(move);
		targetOffset = -offsetVec.dotProduct(splineNormal);

		target = splinePos.add(splineDir.rotate(Math.toRadians(90)).normalize().multiply(targetOffset));
		targetChangeTime = 2;
	}
	
	@Override
	public void updateControls(double deltaTime) {
		while (target.subtract(getPos()).getLengthSQ() <= 1) {
			currentT += 0.1;
			if (currentT >= spline.getMaxT() || (currentT > spline.getMaxT() / 2 && getNearestT() == 0)) {
				currentT = DEFAULT_T;
			}
			Vector2D splineTarget = spline.getPoint(currentT);
			//target = target.add(splineTarget.subtract(lastSplineTarget));
			target = splineTarget.add(spline.getDerivative(currentT).rotate(Math.toRadians(90)).normalize().multiply(targetOffset));
		}
		
		targetChangeTime -= deltaTime;
		if (targetChangeTime < 0) {
			targetChangeTime += 2;
			targetOffsetChange = 0.6 * Math.random() - 0.3;
		}
		targetOffset = Math.max(Math.min(targetOffset + targetOffsetChange * deltaTime, Constants.ROAD_AI_WIDTH), -Constants.ROAD_AI_WIDTH);
		Vector2D splineTarget = spline.getPoint(currentT);
		target = splineTarget.add(spline.getDerivative(currentT).rotate(Math.toRadians(90)).normalize().multiply(targetOffset));
		
		Vector2D targetDir = target.subtract(getPos()).normalize();
		setDir(targetDir);
		
		
		acceleration = maxAcceleration;
	}
}
