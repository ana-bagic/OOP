package lab3.z2.notepad.actions;

import java.util.LinkedList;
import java.util.List;

import lab3.z2.notepad.model.Location;
import lab3.z2.notepad.model.TextEditorModel;

public class DeleteAction implements EditAction {

	private TextEditorModel model;
	private boolean before;
	private List<String> prevLines;
	private Location prevCursor;
	
	public DeleteAction(TextEditorModel model, boolean before) {
		this.model = model;
		this.before = before;
	}
	
	@Override
	public void executeDo() {
		List<String> lines = model.getLines();
		prevLines = new LinkedList<>(lines);
		Location cursorLoc = model.getCursorLocation();
		prevCursor = new Location(cursorLoc);
		
		if(before) {
			if(cursorLoc.getX() == 0 && cursorLoc.getY() == 0)
				return;
			
			model.moveCursorLeft();
		}
		
		int x = cursorLoc.getX(), y = cursorLoc.getY();
		
		if(x == lines.get(y).length()) {
			if(y == lines.size() - 1) {
				return;
			} else {
				lines.set(y, lines.get(y) + lines.remove(y + 1));
			}
		} else {
			StringBuilder sb = new StringBuilder(lines.get(y));
			lines.set(y, sb.deleteCharAt(x).toString());
		}
		
		model.notifyTextObservers();
		model.notifyCursorObservers();
	}

	@Override
	public void executeUndo() {
		model.setLines(prevLines);
		model.setCursorLocation(prevCursor);
		model.notifyTextObservers();
		model.notifyCursorObservers();
	}

}
