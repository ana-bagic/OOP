package lab3.z2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class GraphicComponent extends JComponent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int H = getHeight();
		int W = getWidth();
		
		g2.setColor(Color.RED);
		g2.drawLine(20, H/4, W/2, H/4);
		g2.drawLine(3*W/4, 20, 3*W/4, H/2);
		
		g2.setColor(Color.BLACK);
		String text1 = "Neki dugacki tekst da bih provjerila kako funkcionira ovo";
		String text2 = "Neki drugi dugacki tekst da bih provjerila kako funkcionira ovo u drugom redu";
		
		int startText1 = H/2 + 40;
		int startText2 = startText1 + g2.getFontMetrics().getHeight();
		g2.drawString(text1, 20, startText1);
		g2.drawString(text2, 20, startText2);	
	}
}
