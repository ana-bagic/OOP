package lab4.state;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;

import lab4.document.DocumentModel;
import lab4.model.CompositeShape;
import lab4.model.GraphicalObject;
import lab4.model.Point;
import lab4.model.Rectangle;
import lab4.render.Renderer;

public class SelectShapeState extends IdleState {
	
	private DocumentModel model;
	
	public SelectShapeState(DocumentModel model) {
		this.model = model;
	}
	
	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		GraphicalObject go = model.findSelectedGraphicalObject(mousePoint);
		if(go == null) {
			model.deselectAll();
			return;
		}
		
		if(!ctrlDown) {
			model.deselectAll();
			go.setSelected(true);
		} else {
			go.setSelected(!go.isSelected());
		}
	}
	
	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		List<GraphicalObject> selected = model.getSelectedObjects();
		if(selected.size() == 1) {
			GraphicalObject go = selected.get(0);
			int index = model.findSelectedHotPoint(go, mousePoint);
			
			if(index != -1) {
				go.setHotPointSelected(index, false);
			}
		}
	}
	
	@Override
	public void mouseDragged(Point mousePoint) {
		List<GraphicalObject> selected = model.getSelectedObjects();
		if(selected.size() == 1) {
			GraphicalObject go = selected.get(0);
			int index = model.findSelectedHotPoint(go, mousePoint);
			
			if(index != -1) {
				go.setHotPointSelected(index, true);
				go.setHotPoint(index, mousePoint);
			}
		}
	}
	
	@Override
	public void keyPressed(int keyCode) {
		switch(keyCode) {
		case KeyEvent.VK_LEFT ->
			model.getSelectedObjects().forEach(o -> o.translate(new Point(-1, 0)));
		case KeyEvent.VK_RIGHT ->
			model.getSelectedObjects().forEach(o -> o.translate(new Point(1, 0)));
		case KeyEvent.VK_UP ->
			model.getSelectedObjects().forEach(o -> o.translate(new Point(0, -1)));
		case KeyEvent.VK_DOWN ->
			model.getSelectedObjects().forEach(o -> o.translate(new Point(0, 1)));
		case KeyEvent.VK_PLUS -> {
			List<GraphicalObject> selected = model.getSelectedObjects();
			if(selected.size() == 1) {
				model.increaseZ(selected.get(0));
			}
		}
		case KeyEvent.VK_MINUS -> {
			List<GraphicalObject> selected = model.getSelectedObjects();
			if(selected.size() == 1) {
				model.decreaseZ(selected.get(0));
			}
		}
		case KeyEvent.VK_G -> {
			List<GraphicalObject> selected = new ArrayList<>(model.getSelectedObjects());
			if(selected.size() > 1) {
				selected.forEach(o -> model.removeGraphicalObject(o));
				model.addGraphicalObject(new CompositeShape(selected, true));	
			}
		}
		case KeyEvent.VK_U -> {
			List<GraphicalObject> selected = model.getSelectedObjects();
			if(selected.size() == 1 && selected.get(0) instanceof CompositeShape) {
				CompositeShape cs = (CompositeShape) selected.get(0);
				model.removeGraphicalObject(cs);
				cs.forEach(o -> {
					o.setSelected(true);
					model.addGraphicalObject(o);
				});
			}
		}
		}
	}

	@Override
	public void afterDraw(Renderer r, GraphicalObject go) {
		if(!go.isSelected()) return;
		
		drawRectangle(go.getBoundingBox(), r);
		
		int HOT_POINT_W = 3;
		List<GraphicalObject> selected = model.getSelectedObjects();
		if(selected.size() == 1 && go.equals(selected.get(0))) {
			for(int i = 0; i < go.getNumberOfHotPoints(); i++) {
				Point p = go.getHotPoint(i);
				drawRectangle(new Rectangle(p.getX() - HOT_POINT_W, p.getY() - HOT_POINT_W, HOT_POINT_W*2, HOT_POINT_W*2), r);
			}
		}
	}
	
	@Override
	public void onLeaving() {
		model.deselectAll();
	}
	
	private void drawRectangle(Rectangle rect, Renderer r) {
		int x = rect.getX(), y = rect.getY(), w = rect.getWidth(), h = rect.getHeight();
		
		r.drawLine(new Point(x, y), new Point(x + w, y));
		r.drawLine(new Point(x + w, y), new Point(x + w, y + h));
		r.drawLine(new Point(x, y + h), new Point(x + w, y + h));
		r.drawLine(new Point(x, y), new Point(x, y + h));
	}
	
}
