package lab4;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import lab4.document.DocumentModel;
import lab4.model.CompositeShape;
import lab4.model.GraphicalObject;
import lab4.model.LineSegment;
import lab4.model.Oval;
import lab4.model.Point;
import lab4.render.G2DRendererImpl;
import lab4.render.Renderer;
import lab4.render.SVGRendererImpl;
import lab4.state.AddShapeState;
import lab4.state.EraserState;
import lab4.state.IdleState;
import lab4.state.SelectShapeState;
import lab4.state.State;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private List<GraphicalObject> objects;
	private DocumentModel model;
	private DrawingCanvas canvas;
	private State currentState = new IdleState();
	private Map<String, GraphicalObject> prototypes = new HashMap<>();

	public GUI(List<GraphicalObject> objects) {
		this.objects = objects;
		
		model = new DocumentModel();
		canvas = new DrawingCanvas();
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Program za uređivanje vektorskih crteža");
		setLocationRelativeTo(null);
		setSize(600, 600);
		
		initGUI();
	}

	private void initGUI() {
		JToolBar toolBar = new JToolBar("Tools");
		toolBar.setFloatable(true);
		
		JButton load = new JButton("Učitaj");
		load.addActionListener(e -> {
			String path = choosepath("Učitaj", ".txt");
			if(path == null) return;
			List<String> rows = new LinkedList<>();
			try(Scanner sc = new Scanner(new File(path))) {
				while(sc.hasNext()) rows.add(sc.nextLine());
				Stack<GraphicalObject> stack = new Stack<>();
				
				rows.forEach(r -> {
					String data[] = r.split(" ", 2);
					prototypes.get(data[0]).load(stack, data[1]);
				});
				
				model.clear();
				stack.forEach(o -> model.addGraphicalObject(o));
				JOptionPane.showMessageDialog(this, "File successfully loaded.", "Success", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, "Error while loading file " + path, "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		toolBar.add(load);
		
		JButton save = new JButton("Pohrani");
		save.addActionListener(e -> {
			String path = choosepath("Pohrani", ".txt");
			if(path == null) return;
			List<String> rows = new LinkedList<>();
			model.list().forEach(o -> o.save(rows));
			
			try {
				FileWriter fw = new FileWriter(new File(path));
				for (String l : rows) fw.write(l);
				fw.flush();
				fw.close();
				JOptionPane.showMessageDialog(this, "File successfully saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this, "Error while saving file " + path, "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		toolBar.add(save);
		
		JButton export = new JButton("SVG Export");
		export.addActionListener(e -> {
			String path = choosepath("SVG Export", ".svg");
			if(path == null) return;
			SVGRendererImpl r = new SVGRendererImpl(path);
			model.list().forEach(o -> o.render(r));
			
			try {
				r.close();
				JOptionPane.showMessageDialog(this, "SVG file successfully saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this, "Error while exporting file " + path, "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		toolBar.add(export);
		
		for(GraphicalObject o : objects) {
			JButton button = new JButton(o.getShapeName());
			button.addActionListener(e -> changeState(new AddShapeState(model, o)));
			toolBar.add(button);
			prototypes.put(o.getShapeID(), o);
		}
		CompositeShape cs = new CompositeShape();
		prototypes.put(cs.getShapeID(), cs);
		
		JButton select = new JButton("Selektiraj");
		select.addActionListener(e -> changeState(new SelectShapeState(model)));
		toolBar.add(select);
		
		JButton delete = new JButton("Brisalo");
		delete.addActionListener(e -> changeState(new EraserState(model)));
		toolBar.add(delete);
		
		add(toolBar, BorderLayout.PAGE_START);
		add(canvas, BorderLayout.CENTER);
		
		model.addDocumentModelListener(() -> canvas.repaint());
	}
	
	private String choosepath(String title, String ext) {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(title);
		if(fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return null;
		
		String path = fc.getSelectedFile().getPath();
		return path + (path.endsWith(ext) ? "" : ext);
	}
	
	private void changeState(State state) {
		currentState.onLeaving();
		currentState = state;
	}
	
	private class DrawingCanvas extends JComponent {

		private static final long serialVersionUID = 1L;
		
		public DrawingCanvas() {
			setFocusable(true);
			requestFocusInWindow();
			
			addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						changeState(new IdleState());
					}
					currentState.keyPressed(e.getKeyCode());
				}
			});
			
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					currentState.mouseUp(new Point(e.getX(), e.getY()), e.isShiftDown(), e.isControlDown());
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					currentState.mouseDown(new Point(e.getX(), e.getY()), e.isShiftDown(), e.isControlDown());
				}
			});
			
			addMouseMotionListener(new MouseMotionAdapter() {
				
				@Override
				public void mouseDragged(MouseEvent e) {
					currentState.mouseDragged(new Point(e.getX(), e.getY()));
				}
			});
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;
			Renderer r = new G2DRendererImpl(g2d);
			
			for(GraphicalObject o : model.list()) {
				o.render(r);
				currentState.afterDraw(r, o);
			}
			
			currentState.afterDraw(r);
			requestFocusInWindow();
		}

	}
	
	public static void main(String[] args) {		
		SwingUtilities.invokeLater(() -> {
			List<GraphicalObject> objects = new ArrayList<>();

			objects.add(new LineSegment());
			objects.add(new Oval());

			new GUI(objects).setVisible(true);
		});
	}
}
