package lab3.z2.notepad.actions;

import java.util.LinkedList;
import java.util.List;

import lab3.z2.notepad.model.Location;
import lab3.z2.notepad.model.TextEditorModel;

public class InsertAction implements EditAction {

	private TextEditorModel model;
	private String text;
	private List<String> prevLines;
	private Location prevCursor;
	
	public InsertAction(TextEditorModel model, String text) {
		this.model = model;
		this.text = text;
	}
	
	@Override
	public void executeDo() {
		List<String> lines = model.getLines();
		prevLines = new LinkedList<>(lines);
		Location cursorLoc = model.getCursorLocation();
		prevCursor = new Location(cursorLoc);
		
		String[] textLines = text.split("\n", -1);
		
		String line = lines.get(cursorLoc.getY());
		if(textLines.length == 1) {
			StringBuilder sb = new StringBuilder(line);
			sb.insert(cursorLoc.getX(), textLines[0]);
			lines.set(cursorLoc.getY(), sb.toString());
			
			cursorLoc.updateX(textLines[0].length());
		} else {			
			String firstPart = line.substring(0, cursorLoc.getX());
			String lastPart = line.substring(cursorLoc.getX());
			lines.set(cursorLoc.getY(), firstPart + textLines[0]);
			
			for(int i = 1; i < textLines.length - 1; i++) {
				lines.add(cursorLoc.getY() + i, textLines[i]);
			}
			
			line = textLines[textLines.length - 1];
			cursorLoc.setX(line.length());
			cursorLoc.updateY(textLines.length - 1);
			lines.add(cursorLoc.getY(), line + lastPart);
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
