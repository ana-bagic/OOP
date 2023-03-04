package lab4.state;

import lab4.document.DocumentModel;
import lab4.model.GraphicalObject;
import lab4.model.Point;

public class AddShapeState extends IdleState {
	
	private GraphicalObject prototype;
	private DocumentModel model;
	
	public AddShapeState(DocumentModel model, GraphicalObject prototype) {
		this.model = model;
		this.prototype = prototype;
	}

	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		GraphicalObject go = prototype.duplicate();
		go.translate(mousePoint);
		model.addGraphicalObject(go);
	}

}