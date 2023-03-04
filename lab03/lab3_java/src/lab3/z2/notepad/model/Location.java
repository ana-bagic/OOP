package lab3.z2.notepad.model;

public class Location implements Comparable<Location> {

	private int x;
	private int y;
	
	public Location(Location loc) {
		x = loc.x;
		y = loc.y;
	}
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void updateX(int dx) {
		x += dx;
	}
	
	public void updateY(int dy) {
		y += dy;
	}

	@Override
	public int compareTo(Location o) {
		int byY = Integer.compare(y, o.y);
		if(byY != 0) return byY;
		return Integer.compare(x, o.x);
	}
}
