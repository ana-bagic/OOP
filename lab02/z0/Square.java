package lab2.z0;

public class Square extends Shape {

	double side;
	
	public Square(int x, int y, double side) {
		center = new Point();
		center.x = x;
		center.y = y;
		this.side = side;
	}
	
	@Override
	public void draw() {
		System.out.println("Draw square");
	}

	@Override
	public void move(int x, int y) {
		center.x += x;
		center.y += y;
		System.out.println("Square is moved");
	}

}
