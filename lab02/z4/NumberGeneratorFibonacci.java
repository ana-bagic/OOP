package lab2.z4;

import java.util.LinkedList;
import java.util.List;

public class NumberGeneratorFibonacci implements INumberGenerator {

	private int size;
	
	public NumberGeneratorFibonacci(int size) {
		this.size = size;
	}
	
	@Override
	public List<Integer> generate() {
		List<Integer> numbers = new LinkedList<>();
		
		for(int i = 0; i < size; i++) {
			switch (i) {
			case 0 -> numbers.add(0);
			case 1 -> numbers.add(1);
			default -> numbers.add(numbers.get(i-2) + numbers.get(i-1));
			}
		}
		
		return numbers;
	}

}
