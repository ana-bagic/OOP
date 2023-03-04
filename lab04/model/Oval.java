package lab4.model;

import java.util.List;
import java.util.Stack;

import lab4.render.Renderer;
import lab4.util.GeometryUtil;

public class Oval extends AbstractGraphicalObject {

	private static final int NUMBER_OF_POINTS = 180;
	
	public Oval() {
		super(new Point[] {new Point(10, 0), new Point(0, 10)});
	}
	
	public Oval(Point right, Point down) {
		super(new Point[] {right, down});
	}

	@Override
	public Rectangle getBoundingBox() {
		Point r = getHotPoint(0), d = getHotPoint(1);
		int width = Math.abs(r.getX() - d.getX())*2;
		int height = Math.abs(r.getY() - d.getY())*2;
		return new Rectangle(r.getX() - width, d.getY() - height, width, height);
	}

	@Override
	public double selectionDistance(Point mousePoint) {
		Point r = getHotPoint(0), d = getHotPoint(1);
		int a = r.getX() - d.getX();
		int b = d.getY() - r.getY();
				
		double elipseEquation = Math.pow(mousePoint.getX() - d.getX(), 2)/Math.pow(a, 2) + Math.pow(mousePoint.getY() - r.getY(), 2)/Math.pow(b, 2);
		
		if(elipseEquation <= 1) {
			return 0;
		}
		
		double min = Double.MAX_VALUE;
		
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			double angle = 2*Math.PI*i / NUMBER_OF_POINTS;
			int x = (int)(a * Math.cos(angle)) + d.getX();
			int y = (int)(b * Math.sin(angle)) + r.getY();
			
			double distance = GeometryUtil.distanceFromPoint(new Point(x, y), mousePoint);
			
			if(distance < min) {
				min = distance;
			}
		}
		
		return min;
	}

	@Override
	public String getShapeName() {
		return "Oval";
	}

	@Override
	public GraphicalObject duplicate() {
		return new Oval(getHotPoint(0), getHotPoint(1));
	}

	@Override
	public void render(Renderer r) {
		int x0 = getHotPoint(0).getX();
		int y0 = getHotPoint(0).getY();
		
		int a = x0 - getHotPoint(1).getX();
		int b = getHotPoint(1).getY() - y0;
		
		int centerX = x0 - a;
		int centerY = y0;
		
		Point[] points = new Point[NUMBER_OF_POINTS];
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			double angle = 2*Math.PI*i / NUMBER_OF_POINTS;
			
			int x = (int)(centerX + a * Math.cos(angle) + 0.5);
			int y = (int)(centerY + b * Math.sin(angle) + 0.5);
			
			points[i] = new Point(x, y);
		}
		
		r.fillPolygon(points);
	}

	@Override
	public String getShapeID() {
		return "@OVAL";
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		String points[] = data.split(" ");
		int rx = Integer.parseInt(points[0]);
		int ry = Integer.parseInt(points[1]);
		int dx = Integer.parseInt(points[2]);
		int dy = Integer.parseInt(points[3]);
		
		stack.push(new Oval(new Point(rx, ry), new Point(dx, dy)));
	}

	@Override
	public void save(List<String> rows) {
		Point r = getHotPoint(0), d = getHotPoint(1);
		rows.add(String.format("%s %d %d %d %d\n", getShapeID(), r.getX(), r.getY(), d.getX(), d.getY()));
	}

}
