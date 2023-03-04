package lab3.z2.notepad.plugins;

import lab3.z2.notepad.ClipboardStack;
import lab3.z2.notepad.actions.UndoManager;
import lab3.z2.notepad.model.TextEditorModel;

public interface Plugin {

	public String getName();
	public String getDescription();
	public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack);
}