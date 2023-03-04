package lab2.z6;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Cell implements CellObserver {
	
	private Sheet sheet;
	private String exp;
	private double value;
	private List<CellObserver> observers;
	
	public Cell(Sheet sheet, String exp) {
		this.sheet = sheet;
		this.exp = exp;
		observers = new LinkedList<>();
		calculateValue();
	}

	private void calculateValue() {
		try {
			value = Double.parseDouble(exp);
		} catch (NumberFormatException e) {
			String els[] = exp.split("\\+");
			value = 0;
			for(String s: els) {
				value += sheet.cell(s).getValue();
			}
		}
		observers.forEach(o -> o.notifyCell());
	}

	public void setExp(String exp) {
		this.exp = exp;
		calculateValue();
	}
	
	public double getValue() {
		return value;
	}
	
	public String getExp() {
		return exp;
	}
	
	public void addObserver(CellObserver observer) {
		observers.add(Objects.requireNonNull(observer));
	}
	
	public void removeObserver(CellObserver observer) {
		observers.remove(observer);
	}
	
	public List<CellObserver> getObservers() {
		return observers;
	}

	@Override
	public void notifyCell() {
		calculateValue();
	}
}
