package lab3.z2;

import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class GraphicFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public GraphicFrame() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("GraphicTest");
		setLocation(0, 0);
		setSize(600, 600);
		
		initGUI();
	}

	private void initGUI() {
		GraphicComponent comp = new GraphicComponent();
		
		comp.setFocusable(true);
		comp.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					dispose();
				}
			}
		});
		
		Container cp = getContentPane();
		cp.add(comp);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new GraphicFrame().setVisible(true));
	}
}
