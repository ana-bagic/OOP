package lab3.z2.notepad.actions;

import java.util.LinkedList;
import java.util.List;

import lab3.z2.notepad.model.Location;
import lab3.z2.notepad.model.LocationRange;
import lab3.z2.notepad.model.TextEditorModel;

public class DeleteRangeAction implements EditAction {

	private TextEditorModel model;
	private List<String> prevLines;
	private Location prevCursor;
	private LocationRange prevRange;
	
	public DeleteRangeAction(TextEditorModel model) {
		this.model = model;
	}
	
	@Override
	public void executeDo() {
		List<String> lines = model.getLines();
		prevLines = new LinkedList<>(lines);
		Location cursorLoc = model.getCursorLocation();
		prevCursor = new Location(cursorLoc);
		LocationRange range = model.getSelectionRange();
		prevRange = new LocationRange(range);
		
		int startX = range.getFirst().getX(), startY = range.getFirst().getY();
		int endX = range.getLast().getX(), endY = range.getLast().getY();
		
		lines.set(startY, lines.get(startY).substring(0, startX) + lines.get(endY).substring(endX));
		for (int i = startY + 1; i <= endY; i++) {
			lines.remove(i);
		}

		cursorLoc.setLocation(startX, startY);
		
		model.setSelectionRange(null);
		model.notifyTextObservers();
		model.notifyCursorObservers();
	}

	@Override
	public void executeUndo() {
		model.setLines(prevLines);
		model.setCursorLocation(prevCursor);
		model.setSelectionRange(prevRange);
		model.notifyTextObservers();
		model.notifyCursorObservers();
	}
}
