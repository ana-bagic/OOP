package lab3.z2.notepad;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;

import javax.swing.JComponent;

import lab3.z2.notepad.model.Location;
import lab3.z2.notepad.model.LocationRange;
import lab3.z2.notepad.model.TextEditorModel;

public class TextEditorArea extends JComponent {

	private static final long serialVersionUID = 1L;
	private TextEditorModel model;

	public TextEditorArea(TextEditorModel model) {
		this.model = model;
		setFocusable(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		FontMetrics fm = g2.getFontMetrics();
		
		int margin = 10;
		int lineHeight = fm.getHeight() + fm.getDescent();
		
		if(model.getSelectionRange() != null) {
			LocationRange selection = model.getSelectionRange();
			Location start = selection.getFirst();
			Location end = selection.getLast();
			
			g2.setColor(Color.LIGHT_GRAY);
			
			String line = model.lineAt(start.getY());
			int xStart = margin + fm.stringWidth(line.substring(0, start.getX()));
			int yStart = 5 + start.getY() * lineHeight;
			if(start.getY() == end.getY()) {
				g2.fillRect(xStart, yStart, fm.stringWidth(line.substring(start.getX(), end.getX())), lineHeight);
			} else {
				g2.fillRect(xStart, yStart, fm.stringWidth(line.substring(start.getX())), lineHeight);
				
				for (int i = start.getY() + 1; i < end.getY(); i++) {
					g2.fillRect(margin, 5 + i * lineHeight, fm.stringWidth(model.lineAt(i)), lineHeight);
				}
				
				line = model.lineAt(end.getY());
				g2.fillRect(margin, 5 + end.getY() * lineHeight, fm.stringWidth(line.substring(0, end.getX())), lineHeight);
			}
		}
		
		g2.setColor(Color.BLACK);
		
		Iterator<String> iterator = model.allLines();
		for(int i = 1; iterator.hasNext(); i++) {
			String line = iterator.next();
			g2.drawString(line, margin, i*lineHeight);
		}
		
		Location cursorLocation = model.getCursorLocation();
		String line = "";
		if(model.hasLines()) {
			line = model.lineAt(cursorLocation.getY()).substring(0, cursorLocation.getX());
		}
		
		int x = fm.stringWidth(line) + margin;
		int y1 = (cursorLocation.getY() + 1) * lineHeight;
		int y2 = y1 - fm.getAscent();
		
		g2.drawLine(x, y1, x, y2);
	}
}
