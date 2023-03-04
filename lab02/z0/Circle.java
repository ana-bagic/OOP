package lab2.z0;

public class Circle extends Shape {

	double radius;
	
	public Circle(int x, int y, double radius) {
		center = new Point();
		center.x = x;
		center.y = y;
		this.radius = radius;
	}
	
	@Override
	public void draw() {
		System.out.println("Draw circle");
	}

	@Override
	public void move(int x, int y) {
		center.x += x;
		center.y += y;
		System.out.println("Circle is moved");
	}

}
