package lab2.z0;

import java.util.LinkedList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		List<Shape> shapes = new LinkedList<>();
		shapes.add(new Circle(0, 0, 2));
		shapes.add(new Square(1, 2, 4));
		shapes.add(new Square(2, 5, 10));
		shapes.add(new Circle(1, 1, 1));
		shapes.add(new Rhomb(5, 5, 2, 1));
		
		drawShapes(shapes);
		moveShapes(shapes, 2, 2);
	}
	
	private static void drawShapes(List<Shape> shapes) {
		shapes.forEach(s -> s.draw());
	}
	
	private static void moveShapes(List<Shape> shapes, int x, int y) {
		shapes.forEach(s -> s.move(x, y));
	}
}
