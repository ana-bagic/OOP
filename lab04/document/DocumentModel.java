package lab4.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lab4.listeners.DocumentModelListener;
import lab4.listeners.GraphicalObjectListener;
import lab4.model.GraphicalObject;
import lab4.model.Point;

public class DocumentModel {

	public final static double SELECTION_PROXIMITY = 10;

	private List<GraphicalObject> objects = new ArrayList<>();
	private List<GraphicalObject> roObjects = Collections.unmodifiableList(objects);
	private List<DocumentModelListener> listeners = new ArrayList<>();
	private List<GraphicalObject> selectedObjects = new ArrayList<>();
	private List<GraphicalObject> roSelectedObjects = Collections.unmodifiableList(selectedObjects);

	private final GraphicalObjectListener goListener = new GraphicalObjectListener() {
		
		@Override
		public void graphicalObjectSelectionChanged(GraphicalObject go) {
			if(!go.isSelected()) {
				selectedObjects.remove(go);
			} else if(!selectedObjects.contains(go)) {
				selectedObjects.add(go);
			}
		}
		
		@Override
		public void graphicalObjectChanged(GraphicalObject go) {
			notifyListeners();
		}
	};

	public void clear() {
		objects.forEach(o -> o.removeGraphicalObjectListener(goListener));
		objects.clear();
		selectedObjects.clear();
		
		notifyListeners();
	}

	public void addGraphicalObject(GraphicalObject obj) {
		obj.addGraphicalObjectListener(goListener);
		objects.add(obj);
		if(obj.isSelected()) selectedObjects.add(obj);
		
		notifyListeners();
	}

	public void removeGraphicalObject(GraphicalObject obj) {
		obj.removeGraphicalObjectListener(goListener);
		objects.remove(obj);
		selectedObjects.remove(obj);
		
		notifyListeners();
	}

	public List<GraphicalObject> list() {
		return roObjects;
	}

	public void addDocumentModelListener(DocumentModelListener l) {
		listeners.add(l);
	}

	public void removeDocumentModelListener(DocumentModelListener l) {
		listeners.remove(l);
	}

	public void notifyListeners() {
		listeners.forEach(l -> l.documentChange());
	}

	public List<GraphicalObject> getSelectedObjects() {
		return roSelectedObjects;
	}
	
	public void deselectAll() {
		objects.forEach(o -> o.setSelected(false));
	}

	public void increaseZ(GraphicalObject go) {
		int index = objects.indexOf(go);
		if(index == objects.size() - 1) return;
		
		GraphicalObject o = objects.get(index + 1);
		objects.set(index, o);
		objects.set(index + 1, go);
		
		notifyListeners();
	}
	
	public void decreaseZ(GraphicalObject go) {
		int index = objects.indexOf(go);
		if(index == 0) return;
		
		GraphicalObject o = objects.get(index - 1);
		objects.set(index, o);
		objects.set(index - 1, go);
		
		notifyListeners();
	}
	
	public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
		GraphicalObject closest = null;
		double minDistance = SELECTION_PROXIMITY;
		
		for(GraphicalObject o : objects) {
			double distance = o.selectionDistance(mousePoint);
			if(distance <= minDistance) {
				closest = o;
				minDistance = distance;
			}
		}
		
		return closest;
	}

	public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
		int closest = -1;
		double minDistance = SELECTION_PROXIMITY;
		
		for(int i = 0; i < object.getNumberOfHotPoints(); i++) {
			double distance = object.getHotPointDistance(i, mousePoint);
			if(distance <= minDistance) {
				closest = i;
				minDistance = distance;
			}
		}
		
		return closest;
	}
}
