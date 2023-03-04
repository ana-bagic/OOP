package lab4.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import lab4.listeners.GraphicalObjectListener;
import lab4.render.Renderer;

public class CompositeShape implements GraphicalObject, Iterable<GraphicalObject> {

	private List<GraphicalObject> objects;
	private boolean selected = false;
	private List<GraphicalObjectListener> listeners = new LinkedList<>();
	
	public CompositeShape() {}
	
	public CompositeShape(List<GraphicalObject> objects, boolean selected) {
		this.objects = objects;
		this.selected = selected;
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
	public int getNumberOfHotPoints() {
		return 0;
	}

	@Override
	public Point getHotPoint(int index) {
		return null;
	}

	@Override
	public void setHotPoint(int index, Point point) {}

	@Override
	public boolean isHotPointSelected(int index) {
		return false;
	}

	@Override
	public void setHotPointSelected(int index, boolean selected) {}

	@Override
	public double getHotPointDistance(int index, Point mousePoint) {
		return Double.MAX_VALUE;
	}

	@Override
	public void translate(Point delta) {
		objects.forEach(o -> o.translate(delta));
		notifyListeners();
	}

	@Override
	public Rectangle getBoundingBox() {
		if(!objects.isEmpty()) {
			Rectangle rect = objects.get(0).getBoundingBox();
			int minX = rect.getX(), minY = rect.getY();
			int maxX = minX + rect.getWidth(), maxY = minY + rect.getHeight();
			
			for(GraphicalObject o : objects) {
				rect = o.getBoundingBox();
				minX = Math.min(minX, rect.getX());
				minY = Math.min(minY, rect.getY());
				maxX = Math.max(maxX, rect.getX() + rect.getWidth());
				maxY = Math.max(maxY, rect.getY() + rect.getHeight());
			}
			
			return new Rectangle(minX, minY, maxX - minX, maxY - minY);
		}
		
		return null;
	}

	@Override
	public double selectionDistance(Point mousePoint) {
		if(objects.isEmpty()) {
			return Double.MAX_VALUE;
		}
		
		List<Double> distances = new LinkedList<>();
		objects.forEach(o -> distances.add(o.selectionDistance(mousePoint)));
		
		return distances.stream().min((o1, o2) -> o1.compareTo(o2)).get();
	}

	@Override
	public void render(Renderer r) {
		objects.forEach(o -> o.render(r));
	}

	@Override
	public void addGraphicalObjectListener(GraphicalObjectListener l) {
		listeners.add(l);
	}

	@Override
	public void removeGraphicalObjectListener(GraphicalObjectListener l) {
		listeners.remove(l);
	}

	@Override
	public String getShapeName() {
		return "Composite";
	}

	@Override
	public GraphicalObject duplicate() {
		return new CompositeShape(new ArrayList<>(objects), false);
	}

	@Override
	public String getShapeID() {
		return "@COMP";
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		List<GraphicalObject> objects = new LinkedList<>();
		Stack<GraphicalObject> tmpStack = new Stack<>();
		for(int i = 0; i < Integer.parseInt(data); i++) {
			tmpStack.push(stack.pop());
		}
		
		while(!tmpStack.isEmpty()) {
			objects.add(tmpStack.pop());
		}
		
		stack.push(new CompositeShape(objects, false));
	}

	@Override
	public void save(List<String> rows) {
		objects.forEach(o -> o.save(rows));
		rows.add(String.format("%s %d\n", getShapeID(), objects.size()));
	}

	private void notifyListeners() {
		listeners.forEach(l -> l.graphicalObjectChanged(this));
	}
	
	private void notifySelectionListeners() {
		listeners.forEach(l -> l.graphicalObjectSelectionChanged(this));
	}

	@Override
	public Iterator<GraphicalObject> iterator() {
		return objects.iterator();
	}
}
