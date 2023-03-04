package lab4.render;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lab4.model.Point;

public class SVGRendererImpl implements Renderer {

	private List<String> lines = new ArrayList<>();
	private String fileName;
	
	public SVGRendererImpl(String fileName) {
		this.fileName = fileName;
		lines.add("<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">");
	}

	public void close() throws IOException {
		lines.add("</svg>");
		FileWriter fw = new FileWriter(new File(fileName));
		for (String l : lines) fw.write(l);
		fw.flush();
		fw.close();
	}
	
	@Override
	public void drawLine(Point s, Point e) {
		lines.add(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:#0000ff;\"/>", s.getX(), s.getY(), e.getX(), e.getY()));
	}

	@Override
	public void fillPolygon(Point[] points) {
		StringBuilder sb = new StringBuilder("<polygon points=\"");
		for (Point p : points) {
			sb.append(p.getX()).append(',').append(p.getY()).append(' ');
		}
		sb.append("\" style=\"stroke:#ff0000; fill:#0000ff;\"/>");
		lines.add(sb.toString());
	}

}