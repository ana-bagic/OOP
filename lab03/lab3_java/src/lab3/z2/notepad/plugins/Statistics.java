package lab3.z2.notepad.plugins;

import java.util.Iterator;

import javax.swing.JOptionPane;

import lab3.z2.notepad.ClipboardStack;
import lab3.z2.notepad.actions.UndoManager;
import lab3.z2.notepad.model.TextEditorModel;

public class Statistics implements Plugin {

	@Override
	public String getName() {
		return "Statistics";
	}

	@Override
	public String getDescription() {
		return "Shows the statistics of the document.";
	}

	@Override
	public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
		int nrOfChars = 0, nrOfLines = 0, nrOfWords = 0;
		
		Iterator<String> iterator = model.allLines();
		while(iterator.hasNext()) {
			String line = iterator.next();
			nrOfChars += line.length();
			nrOfWords += line.split(" ").length;
			nrOfLines++;
		}
		
		JOptionPane.showMessageDialog(null, "Document contains " + nrOfChars + " letters, "
		+ nrOfWords + " words and " + nrOfLines + " lines.", "Statistics", JOptionPane.INFORMATION_MESSAGE);
	}

}
