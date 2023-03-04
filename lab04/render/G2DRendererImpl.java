package lab4.render;

import java.awt.Color;
import java.awt.Graphics2D;

import lab4.model.Point;

public class G2DRendererImpl implements Renderer {

	private Graphics2D g2d;
	
	public G2DRendererImpl(Graphics2D g2d) {
		this.g2d = g2d;
	}

	@Override
	public void drawLine(Point s, Point e) {
		g2d.setColor(Color.BLUE);
		g2d.drawLine(s.getX(), s.getY(), e.getX(), e.getY());
	}

	@Override
	public void fillPolygon(Point[] points) {
		g2d.setColor(Color.BLUE);
		int[] xPoints = new int[points.length];
		int[] yPoints = new int[points.length];
		int i = 0;
		for(Point p : points) {
			xPoints[i] = p.getX();
			yPoints[i++] = p.getY();
		}
		g2d.fillPolygon(xPoints, yPoints, points.length);
		
		g2d.setColor(Color.RED);
		g2d.drawPolygon(xPoints, yPoints, points.length);
	}

}