package lab2.z4;

import java.util.LinkedList;
import java.util.List;

public class NumberGeneratorLinear implements INumberGenerator {
	
	private int low;
	private int high;
	private int inc;
	
	public NumberGeneratorLinear(int low, int high, int inc) {
		this.low = low;
		this.high = high;
		this.inc = inc;
	}

	@Override
	public List<Integer> generate() {
		List<Integer> numbers = new LinkedList<>();
		
		for(int i = low; i <= high; i += inc) {
			numbers.add(i);
		}
		
		return numbers;
	}

}
