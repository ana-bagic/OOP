package lab2.z4;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NumberGeneratorGauss implements INumberGenerator {

	private int size;
	private double mean;
	private double dev;
	
	public NumberGeneratorGauss(int size, double mean, double dev) {
		this.size = size;
		this.mean = mean;
		this.dev = dev;
	}

	@Override
	public List<Integer> generate() {
		Random rand = new Random();
		List<Integer> numbers = new LinkedList<>();
		
		for(int i = 0; i < size; i++) {
			numbers.add((int)Math.floor(rand.nextGaussian()*dev + mean));
		}
		
		return numbers;
	}
	
}
