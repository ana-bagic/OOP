package lab3.z2.notepad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import lab3.z2.notepad.actions.UndoManager;
import lab3.z2.notepad.model.Location;
import lab3.z2.notepad.model.TextEditorModel;
import lab3.z2.notepad.observers.ClipboardObserver;
import lab3.z2.notepad.observers.CursorObserver;
import lab3.z2.notepad.observers.StackObserver;
import lab3.z2.notepad.plugins.Plugin;

public class TextEditor extends JFrame {

	private static final long serialVersionUID = 1L;
	private TextEditorModel model;
	private TextEditorArea area;
	private ClipboardStack clipboard = new ClipboardStack();
	private Path filePath;
	private static final String PLUGINS_PATH = "plugins";

	public TextEditor() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Notepad");
		setLocationRelativeTo(null);
		setSize(600, 600);
		
		initGUI();
	}

	private void initGUI() {
		initModel("some text\nsome other long text");
		initArea();
		
		createMenuAndToolBar();
		createStatusBar();
		
		Container cp = getContentPane();
		cp.add(area);
	}

	private void initModel(String text) {
		model = new TextEditorModel(text);
		model.addCursorObserver(l -> area.repaint());
		model.addTextObserver(() -> area.repaint());
	}
	
	private void initArea() {
		area = new TextEditorArea(model);
		
		area.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.isControlDown()) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_C -> {
						String range = model.getRange();
						if(range != null) {
							clipboard.push(range);
						}
					}
					case KeyEvent.VK_X-> {
						String range = model.getRange();
						if(range != null) {
							clipboard.push(range);
							model.deleteRange();
						}
					}
					case KeyEvent.VK_V-> {
						if(!clipboard.isEmpty()) {
							if(e.isShiftDown()) {
								model.insert(clipboard.pop());
							} else {
								model.insert(clipboard.peek());
							}
						}
					}
					case KeyEvent.VK_Z-> {
						UndoManager.getInstance().undo();
					}
					case KeyEvent.VK_Y-> {
						UndoManager.getInstance().redo();
					}
					}
				} else {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT -> {
						if(e.isShiftDown()) {
							model.moveSelection(() -> model.moveCursorLeft());
						} else {
							model.moveCursorLeft();
							model.setSelectionRange(null);
						}
					}
					case KeyEvent.VK_RIGHT -> {
						if(e.isShiftDown()) {
							model.moveSelection(() -> model.moveCursorRight());
						} else {
							model.moveCursorRight();
							model.setSelectionRange(null);
						}
					}
					case KeyEvent.VK_UP -> {
						if(e.isShiftDown()) {
							model.moveSelection(() -> model.moveCursorUp());
						} else {
							model.moveCursorUp();
							model.setSelectionRange(null);
						}
					}
					case KeyEvent.VK_DOWN -> {
						if(e.isShiftDown()) {
							model.moveSelection(() -> model.moveCursorDown());
						} else {
							model.moveCursorDown();
							model.setSelectionRange(null);
						}
					}
				}
				}
			}
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(!e.isControlDown()) {
					switch (e.getKeyChar()) {
					case KeyEvent.VK_BACK_SPACE -> {
						if(model.getSelectionRange() == null) {
							model.deleteBefore();
						} else {
							model.deleteRange();
						}
					}
					case KeyEvent.VK_DELETE -> {
						if(model.getSelectionRange() == null) {
							model.deleteAfter();
						} else {
							model.deleteRange();
						}
					}
					default -> {
						char c = e.getKeyChar();
						if(Character.isAlphabetic(c) && e.isShiftDown()) {
							c = Character.toUpperCase(c);
						}
						model.insert(c);
					}
					}
				}
			}
		});
	}
	
	private void createMenuAndToolBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");

		openAction.putValue(Action.NAME, "Open");
		fileMenu.add(new JMenuItem(openAction));
		saveAction.putValue(Action.NAME, "Save");
		fileMenu.add(new JMenuItem(saveAction));
		exitAction.putValue(Action.NAME, "Exit");
		fileMenu.add(new JMenuItem(exitAction));
		menuBar.add(fileMenu);

		JMenu editMenu = new JMenu("Edit");

		UndoAction undo = new UndoAction();
		undo.putValue(Action.NAME, "Undo");
		undo.setEnabled(false);
		UndoManager.getInstance().addUndoObserver(undo);
		editMenu.add(new JMenuItem(undo));
		RedoAction redo = new RedoAction();
		redo.putValue(Action.NAME, "Redo");
		redo.setEnabled(false);
		UndoManager.getInstance().addRedoObserver(redo);
		editMenu.add(new JMenuItem(redo));
		CutAction cut = new CutAction();
		cut.putValue(Action.NAME, "Cut");
		cut.setEnabled(false);
		model.addCursorObserver(cut);
		editMenu.add(new JMenuItem(cut));
		CopyAction copy = new CopyAction();
		copy.putValue(Action.NAME, "Copy");
		copy.setEnabled(false);
		model.addCursorObserver(copy);
		editMenu.add(new JMenuItem(copy));
		PasteAction paste = new PasteAction();
		paste.putValue(Action.NAME, "Paste");
		paste.setEnabled(false);
		clipboard.addObserver(paste);
		editMenu.add(new JMenuItem(paste));
		PasteTakeAction pasteTake = new PasteTakeAction();
		pasteTake.putValue(Action.NAME, "Paste and Take");
		pasteTake.setEnabled(false);
		clipboard.addObserver(pasteTake);
		editMenu.add(new JMenuItem(pasteTake));
		DeleteAction delete = new DeleteAction();
		delete.putValue(Action.NAME, "Delete selection");
		delete.setEnabled(false);
		model.addCursorObserver(delete);
		editMenu.add(new JMenuItem(delete));
		clearAction.putValue(Action.NAME, "Clear document");
		editMenu.add(new JMenuItem(clearAction));
		menuBar.add(editMenu);

		JMenu moveMenu = new JMenu("Move");
		
		moveCursorStart.putValue(Action.NAME, "Cursor to document start");
		moveMenu.add(new JMenuItem(moveCursorStart));
		moveCursorEnd.putValue(Action.NAME, "Cursor to document end");
		moveMenu.add(new JMenuItem(moveCursorEnd));
		menuBar.add(moveMenu);
		
		List<Plugin> plugins = getPlugins();
		if(!plugins.isEmpty()) {
			JMenu pluginMenu = new JMenu("Plugins");
			
			for(Plugin plugin : plugins) {
				Action pluginAction = new AbstractAction() {

					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e) {
						plugin.execute(model, UndoManager.getInstance(), clipboard);
					}
				};
				
				pluginAction.putValue(Action.NAME, plugin.getName());
				pluginAction.putValue(Action.SHORT_DESCRIPTION, plugin.getDescription());
				
				pluginMenu.add(new JMenuItem(pluginAction));
			}
			
			menuBar.add(pluginMenu);
		}
		
		setJMenuBar(menuBar);
		
		JToolBar toolBar = new JToolBar("Tools");
		toolBar.setFloatable(true);
		
		toolBar.add(new JButton(undo));
		toolBar.add(new JButton(redo));
		toolBar.add(new JButton(cut));
		toolBar.add(new JButton(copy));
		toolBar.add(new JButton(paste));
		
		getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}
	
	@SuppressWarnings({ "resource" })
	private List<Plugin> getPlugins() {
		List<Plugin> plugins = new LinkedList<>();
		
		/*
		ClassLoader loader = Plugin.class.getClassLoader();
		
		for(File file : new File(PLUGINS_PATH).listFiles()) {
			if(file.getName().endsWith(".jar")) {
				try(URLClassLoader urlLoader = new URLClassLoader(new URL[] {file.toURI().toURL()}, loader)) {
					Enumeration<JarEntry> e = new JarFile(file).entries();

					while (e.hasMoreElements()) {
					    JarEntry entry = e.nextElement();
					    if(entry.getName().endsWith(".class")){
						    String className = entry.getName().substring(0, entry.getName().length() - 6).replace('/', '.');
						    Class<Plugin> plugin = (Class<Plugin>)urlLoader.loadClass(className);
						    plugins.add(plugin.getConstructor().newInstance());
					    }
					}
				} catch (Exception e) {}
			}
		}
		*/
		
		ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class);
		for (Plugin plugin : loader) {
			plugins.add(plugin);
		}
		
		return plugins;
	}

	private void createStatusBar() {
		JPanel bottomBar = new JPanel(new BorderLayout());
		JPanel statusBar = new JPanel(new GridLayout(1, 3));
		bottomBar.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY));
		JLabel ln = new JLabel(" Ln: 0");
		JLabel col = new JLabel("Col: 0");
		JLabel lines = new JLabel("Lines: " + model.getLines().size());
		statusBar.add(ln);
		statusBar.add(col);
		statusBar.add(lines);
		bottomBar.add(statusBar, BorderLayout.WEST);

		add(bottomBar, BorderLayout.SOUTH);
		
		model.addCursorObserver(l -> {
			ln.setText(" Ln: " + l.getY());
			col.setText("Col: " + l.getX());
		});
		model.addTextObserver(() -> lines.setText("Lines: " + model.getLines().size()));
	}

	private Action openAction = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Load file");
			if (fc.showOpenDialog(TextEditor.this) != JFileChooser.APPROVE_OPTION)
				return;
			
			Path path = fc.getSelectedFile().toPath();
			
			if (!Files.isReadable(path)) {
				JOptionPane.showMessageDialog(TextEditor.this, "File " + path.toAbsolutePath() + " cannot be read.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				byte[] bytes = Files.readAllBytes(path);
				model.reset(new String(bytes, StandardCharsets.UTF_8));
				filePath = path;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(TextEditor.this, "Error while reading file " + path.toAbsolutePath() + ".",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	};
	
	private Action saveAction = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(filePath == null) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Save file");
				if(fc.showSaveDialog(TextEditor.this) != JFileChooser.APPROVE_OPTION)
					return;
				
				filePath = fc.getSelectedFile().toPath();
			}
			
			StringBuilder sb = new StringBuilder();
			Iterator<String> iterator = model.allLines();
			while(iterator.hasNext()) {
				sb.append(iterator.next()).append('\n');
			}
			
			try {
				byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
				Files.write(filePath, bytes);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(TextEditor.this, "Error while saving file " + filePath.toAbsolutePath() + ".",
						"Pogreška", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(TextEditor.this, "File successfully saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
	};
	
	private Action exitAction = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};
	
	private Action clearAction = new AbstractAction() {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.reset("");
		}
	};
	
	private Action moveCursorStart = new AbstractAction() {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.moveCursorStart();
		}
	};
	
	private Action moveCursorEnd = new AbstractAction() {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			model.moveCursorEnd();
		}
	};
	
	class UndoAction extends AbstractAction implements StackObserver {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			UndoManager.getInstance().undo();
		}

		@Override
		public void isEmpty(boolean empty) {
			setEnabled(!empty);
		}
		
	}
	
	class RedoAction extends AbstractAction implements StackObserver {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			UndoManager.getInstance().redo();
		}

		@Override
		public void isEmpty(boolean empty) {
			setEnabled(!empty);
		}
		
	}
	
	class CutAction extends AbstractAction implements CursorObserver {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			clipboard.push(model.getRange());
			model.deleteRange();
		}

		@Override
		public void updateCursorLocation(Location loc) {
			setEnabled(model.getSelectionRange() != null);
		}
		
	}
	
	class CopyAction extends AbstractAction implements CursorObserver {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			clipboard.push(model.getRange());
		}

		@Override
		public void updateCursorLocation(Location loc) {
			setEnabled(model.getSelectionRange() != null);
		}
		
	}
	
	class PasteAction extends AbstractAction implements ClipboardObserver {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.insert(clipboard.peek());
		}

		@Override
		public void updateClipboard() {
			setEnabled(!clipboard.isEmpty());
		}
		
	}
	
	class PasteTakeAction extends AbstractAction implements ClipboardObserver {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.insert(clipboard.pop());
		}

		@Override
		public void updateClipboard() {
			setEnabled(!clipboard.isEmpty());
		}
		
	}
	
	class DeleteAction extends AbstractAction implements CursorObserver {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.deleteRange();
		}

		@Override
		public void updateCursorLocation(Location loc) {
			setEnabled(model.getSelectionRange() != null);
		}
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new TextEditor().setVisible(true));
	}
}
