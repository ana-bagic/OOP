package lab4.util;

import lab4.model.Point;

public class GeometryUtil {

	public static double distanceFromPoint(Point point1, Point point2) {
		double dx2 = Math.pow(point1.getX() - point2.getX(), 2);
		double dy2 = Math.pow(point1.getY() - point2.getY(), 2);
		return Math.sqrt(dx2 + dy2);
	}
	
	public static double distanceFromLineSegment(Point s, Point e, Point p) {		
		double slope = 1.0*(e.getY() - s.getY())/(e.getX() - s.getX());
		double b = s.getY() - slope*s.getX();
		
		double slopePer = -slope;
		double bPer = p.getY() - slopePer*p.getX();
		
		int closestX = (int) ((bPer - b)/(slope - slopePer));
		int closestY = (int) (slope*closestX + b);
		
		Point left = s.getX() < e.getX() ? s : e;
		Point right = s == left ? e : s;
		
		if(closestX <= left.getX()) {
			return distanceFromPoint(p, left);
		}
		
		if(closestX >= right.getX()) {
			return distanceFromPoint(p, right);
		}
		
		return distanceFromPoint(p, new Point(closestX, closestY));
	}
}