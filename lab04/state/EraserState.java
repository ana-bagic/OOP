package lab4.state;

import java.util.LinkedList;
import java.util.List;

import lab4.document.DocumentModel;
import lab4.model.GraphicalObject;
import lab4.model.Point;
import lab4.render.Renderer;

public class EraserState extends IdleState {

	private DocumentModel model;
	private List<Point> line = new LinkedList<>();
	
	public EraserState(DocumentModel model) {
		this.model = model;
	}
	
	@Override
	public void mouseDragged(Point mousePoint) {
		line.add(mousePoint);
		model.notifyListeners();
	}
	
	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		line.add(mousePoint);
		line.forEach(p -> {
			GraphicalObject go = model.findSelectedGraphicalObject(p);
			if(go != null) model.removeGraphicalObject(go);
		});
		line.clear();
		model.notifyListeners();
	}
	
	@Override
	public void afterDraw(Renderer r) {
		for(int i = 0; i < line.size() - 1; i++) {
			r.drawLine(line.get(i), line.get(i + 1));
		}
	}
}
