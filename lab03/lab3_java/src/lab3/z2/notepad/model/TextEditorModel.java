package lab3.z2.notepad.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lab3.z2.notepad.actions.DeleteAction;
import lab3.z2.notepad.actions.DeleteRangeAction;
import lab3.z2.notepad.actions.EditAction;
import lab3.z2.notepad.actions.InsertAction;
import lab3.z2.notepad.actions.UndoManager;
import lab3.z2.notepad.observers.CursorObserver;
import lab3.z2.notepad.observers.TextObserver;

public class TextEditorModel {

	private List<String> lines = new LinkedList<>();
	private LocationRange selectionRange = null;
	private Location cursorLoc = new Location(0, 0);
	private List<CursorObserver> cursorObservers = new LinkedList<>();
	private List<TextObserver> textObservers = new LinkedList<>();
	private UndoManager undoManager = UndoManager.getInstance();
	
	public TextEditorModel(String text) {
		for(String s : text.split("\n", -1))
			lines.add(s);
	}
	
	/***** ITERATOR *****/
	
	public Iterator<String> allLines() {
		return lines.iterator();
	}
	
	public Iterator<String> linesRange(int index1, int index2) {
		return lines.subList(index1, index2).iterator();
	}
	
	/***** LINES *****/
	
	public boolean hasLines() {
		return !lines.isEmpty();
	}
	
	public String lineAt(int index) {
		return lines.get(index);
	}
	
	public List<String> getLines() {
		return lines;
	}
	
	public void setLines(List<String> lines) {
		this.lines = lines;
	}
	
	public void reset(String text) {
		lines.clear();
		cursorLoc = new Location(0, 0);
		selectionRange = null;
		for(String s : text.split("\n", -1))
			lines.add(s);
		notifyTextObservers();
	}
	
	/***** CURSOR *****/
	
	public Location getCursorLocation() {
		return cursorLoc;
	}
	
	public void setCursorLocation(Location location) {
		this.cursorLoc = location;
	}
	
	public void addCursorObserver(CursorObserver observer) {
		cursorObservers.add(observer);
	}
	
	public void removeCursorObserver(CursorObserver observer) {
		cursorObservers.remove(observer);
	}
	
	public void notifyCursorObservers() {
		cursorObservers.forEach(o -> o.updateCursorLocation(cursorLoc));
	}
	
	public void moveCursorLeft() {
		if(cursorLoc.getX() == 0) {
			if(cursorLoc.getY() == 0) {
				return;
			} else {
				cursorLoc.updateY(-1);
				cursorLoc.setX(lines.get(cursorLoc.getY()).length());
			}
		} else {
			cursorLoc.updateX(-1);
		}
		
		notifyCursorObservers();
	}
	
	public void moveCursorRight() {
		if(cursorLoc.getX() == lines.get(cursorLoc.getY()).length()) {
			if(cursorLoc.getY() == lines.size() - 1) {
				return;
			} else {
				cursorLoc.updateY(1);
				cursorLoc.setX(0);
			}
		} else {
			cursorLoc.updateX(1);
		}
		
		notifyCursorObservers();
	}
	
	public void moveCursorUp() {
		if(cursorLoc.getY() != 0) {
			cursorLoc.updateY(-1);
			cursorLoc.setX(Math.min(cursorLoc.getX(), lines.get(cursorLoc.getY()).length()));
			notifyCursorObservers();
		}
	}
	
	public void moveCursorDown() {
		if(cursorLoc.getY() != lines.size() - 1) {
			cursorLoc.updateY(1);
			cursorLoc.setX(Math.min(cursorLoc.getX(), lines.get(cursorLoc.getY()).length()));
			notifyCursorObservers();
		}
	}
	
	public void moveCursorStart() {
		cursorLoc.setLocation(0, 0);
		notifyCursorObservers();
	}
	
	public void moveCursorEnd() {
		cursorLoc.setLocation(lines.get(lines.size() - 1).length(), lines.size() - 1);
		notifyCursorObservers();
	}
	
	/***** TEXT *****/
	
	public void addTextObserver(TextObserver observer) {
		textObservers.add(observer);
	}
	
	public void removeTextObserver(TextObserver observer) {
		textObservers.remove(observer);
	}
	
	public void notifyTextObservers() {
		textObservers.forEach(o -> o.updateText());
	}
	
	public void insert(char c) {
		insert(String.valueOf(c));
	}
	
	public void insert(String text) {
		if(selectionRange != null) {
			deleteRange();
		}
		
		EditAction insert = new InsertAction(this, text);
		insert.executeDo();
		undoManager.push(insert);
	}
	
	public void deleteBefore() {
		EditAction delete = new DeleteAction(this, true);
		delete.executeDo();
		undoManager.push(delete);
	}
	
	public void deleteAfter() {
		EditAction delete = new DeleteAction(this, false);
		delete.executeDo();
		undoManager.push(delete);
	}
	
	/***** SELECTION *****/
	
	public void deleteRange() {
		EditAction delete = new DeleteRangeAction(this);
		delete.executeDo();
		undoManager.push(delete);
	}
	
	public String getRange() {
		if(selectionRange == null) {
			return null;
		}
		
		int startX = selectionRange.getFirst().getX(), startY = selectionRange.getFirst().getY();
		int endX = selectionRange.getLast().getX(), endY = selectionRange.getLast().getY();
		
		String line = lines.get(startY);
		if(startY == endY) {
			return line.substring(startX, endX);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(line.substring(startX)).append('\n');
		for(int i = startY + 1; i < endY; i++) {
			sb.append(lines.get(i)).append('\n');
		}
		sb.append(lines.get(endY).substring(0, endX));
		
		return sb.toString();
	}
	
	public void moveSelection(Runnable move) {
		if(selectionRange == null) {
			selectionRange = new LocationRange(new Location(cursorLoc));
		}
		
		move.run();
		selectionRange.setEnd(cursorLoc);
		notifyCursorObservers();
	}
	
	public LocationRange getSelectionRange() {
		return selectionRange;
	}
	
	public void setSelectionRange(LocationRange selectionRange) {
		this.selectionRange = selectionRange;
	}
	
}
