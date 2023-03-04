package lab2.z0;

public class Rhomb extends Shape {

	double side;
	double angle;
	
	public Rhomb(int x, int y, double side, double angle) {
		center = new Point();
		center.x = x;
		center.y = y;
		this.side = side;
		this.angle = angle;
	}
	
	@Override
	public void draw() {
		System.out.println("Draw rhomb");
	}

	@Override
	public void move(int x, int y) {
		center.x += x;
		center.y += y;
		System.out.println("Rhomb is moved");
	}

}
