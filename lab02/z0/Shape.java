package lab2.z0;

public abstract class Shape {

	class Point {
		int x;
		int y;
	}
	
	Point center;
	
	public abstract void draw();
	public abstract void move(int x, int y);
}
