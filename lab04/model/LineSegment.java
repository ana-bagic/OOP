package lab4.model;

import java.util.List;
import java.util.Stack;

import lab4.render.Renderer;
import lab4.util.GeometryUtil;

public class LineSegment extends AbstractGraphicalObject {

	public LineSegment() {
		super(new Point[] {new Point(0, 0), new Point(10, 0)});
	}
	
	public LineSegment(Point s, Point e) {
		super(new Point[] {s, e});
	}

	@Override
	public Rectangle getBoundingBox() {
		Point s = getHotPoint(0), e = getHotPoint(1);
		return new Rectangle(Math.min(s.getX(), e.getX()), Math.min(s.getY(), e.getY()), Math.abs(s.getX()-e.getX()), Math.abs(s.getY()-e.getY()));
	}

	@Override
	public double selectionDistance(Point mousePoint) {
		return GeometryUtil.distanceFromLineSegment(getHotPoint(0), getHotPoint(1), mousePoint);
	}

	@Override
	public String getShapeName() {
		return "Linija";
	}

	@Override
	public GraphicalObject duplicate() {
		return new LineSegment(getHotPoint(0), getHotPoint(1));
	}

	@Override
	public void render(Renderer r) {
		r.drawLine(getHotPoint(0), getHotPoint(1));
	}

	@Override
	public String getShapeID() {
		return "@LINE";
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		String points[] = data.split(" ");
		int sx = Integer.parseInt(points[0]);
		int sy = Integer.parseInt(points[1]);
		int ex = Integer.parseInt(points[2]);
		int ey = Integer.parseInt(points[3]);
		
		stack.push(new LineSegment(new Point(sx, sy), new Point(ex, ey)));
	}

	@Override
	public void save(List<String> rows) {
		Point s = getHotPoint(0), e = getHotPoint(1);
		rows.add(String.format("%s %d %d %d %d\n", getShapeID(), s.getX(), s.getY(), e.getX(), e.getY()));
	}

}
