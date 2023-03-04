package lab3.z2.notepad.model;

public class LocationRange {

	private Location start;
	private Location end;
	
	public LocationRange(Location start) {
		this.start = start;
	}
	
	public LocationRange(LocationRange range) {
		this.start = new Location(range.start);
		this.end = new Location(range.end);
	}

	public Location getFirst() {
		if(start.compareTo(end) > 0)
			return end;
		return start;
	}
	
	public Location getLast() {
		if(start.compareTo(end) > 0)
			return start;
		return end;
	}
	
	public void setEnd(Location end) {
		this.end = end;
	}
	
}
