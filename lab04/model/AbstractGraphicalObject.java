package lab4.model;

import java.util.ArrayList;
import java.util.List;

import lab4.listeners.GraphicalObjectListener;
import lab4.util.GeometryUtil;

public abstract class AbstractGraphicalObject implements GraphicalObject {

	private Point[] hotpoints;
	private boolean[] hotPointSelected;
	private boolean selected;
	private List<GraphicalObjectListener> listeners = new ArrayList<>();
	
	public AbstractGraphicalObject(Point[] hotPoints) {
		this.hotpoints = hotPoints;
		hotPointSelected = new boolean[hotPoints.length];
	}
	
	@Override
	public Point getHotPoint(int index) {
		return hotpoints[index];
	}
	
	@Override
	public void setHotPoint(int index, Point point) {
		hotpoints[index] = point;
		notifyListeners();
	}
	
	@Override
	public int getNumberOfHotPoints() {
		return hotpoints.length;
	}
	
	@Override
	public double getHotPointDistance(int index, Point mousePoint) {
		return GeometryUtil.distanceFromPoint(hotpoints[index], mousePoint);
	}
	
	@Override
	public boolean isHotPointSelected(int index) {
		return hotPointSelected[index];
	}
	
	@Override
	public void setHotPointSelected(int index, boolean selected) {
		hotPointSelected[index] = selected;
		notifyListeners();
	}
	
	@Override
	public boolean isSelected() {
		return selected;
	}
	
	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
		notifySelectionListeners();
		notifyListeners();
	}
	
	@Override
	public void translate(Point delta) {
		for(int i = 0; i < hotpoints.length; i++) {
			hotpoints[i] = hotpoints[i].translate(delta);
		}
		notifyListeners();
	}
	
	@Override
	public void addGraphicalObjectListener(GraphicalObjectListener l) {
		listeners.add(l);
	}
	
	@Override
	public void removeGraphicalObjectListener(GraphicalObjectListener l) {
		listeners.remove(l);
	}
	
	private void notifyListeners() {
		listeners.forEach(l -> l.graphicalObjectChanged(this));
	}
	
	private void notifySelectionListeners() {
		listeners.forEach(l -> l.graphicalObjectSelectionChanged(this));
	}
	
}
