package lab3.z2.notepad.actions;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import lab3.z2.notepad.observers.StackObserver;

public class UndoManager {

	private static final UndoManager instance = new UndoManager();
	private Stack<EditAction> undoStack = new Stack<>();
	private Stack<EditAction> redoStack = new Stack<>();
	private List<StackObserver> undoObservers = new LinkedList<>();
	private List<StackObserver> redoObservers = new LinkedList<>();
	
	private UndoManager() {}
	
	public static UndoManager getInstance() {
		return instance;
	}
	
	public void undo() {
		if(!undoStack.isEmpty()) {
			EditAction c = undoStack.pop();
			redoStack.push(c);
			c.executeUndo();
			
			notifyUndoObservers(undoStack.isEmpty());
			notifyRedoObservers(false);
		}
	}
	
	public void redo() {
		if(!redoStack.isEmpty()) {
			EditAction c = redoStack.pop();
			undoStack.push(c);
			c.executeDo();
			
			notifyUndoObservers(false);
			notifyRedoObservers(redoStack.isEmpty());
		}
	}
	
	public void push(EditAction c) {
		redoStack.clear();
		undoStack.push(c);
		notifyRedoObservers(true);
		notifyUndoObservers(false);
	}
	
	public void addUndoObserver(StackObserver observer) {
		undoObservers.add(observer);
	}
	
	public void removeUndoObserver(StackObserver observer) {
		undoObservers.remove(observer);
	}
	
	private void notifyUndoObservers(boolean empty) {
		undoObservers.forEach(o -> o.isEmpty(empty));
	}
	
	public void addRedoObserver(StackObserver observer) {
		redoObservers.add(observer);
	}
	
	public void removeRedoObserver(StackObserver observer) {
		redoObservers.remove(observer);
	}
	
	private void notifyRedoObservers(boolean empty) {
		redoObservers.forEach(o -> o.isEmpty(empty));
	}
}
