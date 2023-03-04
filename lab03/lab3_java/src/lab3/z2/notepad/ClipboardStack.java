package lab3.z2.notepad;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import lab3.z2.notepad.observers.ClipboardObserver;

public class ClipboardStack {

	Stack<String> texts = new Stack<>();
	List<ClipboardObserver> observers = new LinkedList<>();
	
	public void push(String text) {
		texts.push(text);
		notifyObservers();
	}
	
	public String pop() {
		String ret = texts.pop();
		notifyObservers();
		return ret;
	}
	
	public String peek() {
		return texts.peek();
	}
	
	public boolean isEmpty() {
		return texts.isEmpty();
	}
	
	public void clear() {
		texts.clear();
		notifyObservers();
	}
	
	public void addObserver(ClipboardObserver observer) {
		observers.add(observer);
	}
	
	public void removeObserver(ClipboardObserver observer) {
		observers.remove(observer);
	}
	
	private void notifyObservers() {
		observers.forEach(o -> o.updateClipboard());
	}
}
