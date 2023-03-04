package lab3.z2.notepad.plugins;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lab3.z2.notepad.ClipboardStack;
import lab3.z2.notepad.actions.UndoManager;
import lab3.z2.notepad.model.TextEditorModel;

public class UpperCase implements Plugin {

	@Override
	public String getName() {
		return "Uppercase";
	}

	@Override
	public String getDescription() {
		return "Every first letter of the words in the document changes to uppercase.";
	}

	@Override
	public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
		List<String> newLines = new LinkedList<>();
		
		Iterator<String> iterator = model.allLines();
		while(iterator.hasNext()) {
			String[] line = iterator.next().split(" ");
			
			StringBuilder sb = new StringBuilder();
			for(String word : line) {
				sb.append(Character.toUpperCase(word.charAt(0)));
				sb.append(word.substring(1));
			}
			
			newLines.add(sb.toString());
		}
		
		model.setLines(newLines);
		model.notifyTextObservers();
	}

}
