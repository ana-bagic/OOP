package lab2.z6;

import java.util.LinkedList;
import java.util.List;

public class Sheet {

	private Cell[][] sheet;
	
	public Sheet(int rows, int columns) {
		sheet = new Cell[rows][columns];
	}
	
	public Cell cell(String ref) {
		int[] pos = calcRef(ref);
		return sheet[pos[0]][pos[1]];
	}
	
	public void set(String ref, String content) {
		if(checkCircular(ref, content)) {
			throw new IllegalArgumentException("Circular dependency");
		}
		int[] pos = calcRef(ref);
		Cell cell = sheet[pos[0]][pos[1]];
		if(cell == null) {
			cell = new Cell(this, content);
			sheet[pos[0]][pos[1]] = cell;
		} else {
			if(!isConstant(cell.getExp())) {
				for(String s: cell.getExp().split("\\+")) {
					cell(s).removeObserver(cell);
				}
			}
			cell.setExp(content);
		}
		
		if(!isConstant(content)) {
			for(String s: content.split("\\+")) {
				cell(s).addObserver(cell);
			}
		}
	}
	
	public List<Cell> getRefs(Cell cell) {
		List<Cell> cells = new LinkedList<>();
		if(!isConstant(cell.getExp())) {
			for(String s: cell.getExp().split("\\+")) {
				cells.add(cell(s));
			}
		}
		return cells;
	}
	
	public double evaluateCell(Cell cell) {
		return cell.getValue();
	}
	
	private int[] calcRef(String ref) {
		try {
			int row = ref.charAt(0) - 65;
			int column = Integer.parseInt(ref.substring(1)) - 1;
			return new int[] {row, column};
		} catch (Exception e) {
			throw new IllegalArgumentException("Index " + ref + " does not exist");
		}
	}
	
	private boolean isConstant(String exp) {
		try {
			Double.parseDouble(exp);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private boolean checkCircular(String ref, String exp) {
		if(!isConstant(exp)) {
			String succ[] = exp.split("\\+");
			boolean ret = false;
			for(String s: succ) {
				if(s.equals(ref)) return true;
				ret = ret || checkCircular(ref, cell(s).getExp());
			}
			return ret;
		}
		return false;
	}
	
	public static void main(String[] args) {
		Sheet s = new Sheet(5, 5);
		s.set("A1","2");
		s.set("A2","5");
		s.set("A3","A1+A2");
		s.set("A1", "4");
		s.set("A4", "A1+A3");
		//s.set("A1", "A3");
		System.out.println("A1 " + s.cell("A1").getValue());
	}
}
