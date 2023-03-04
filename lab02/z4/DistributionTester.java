package lab2.z4;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DistributionTester {

	private INumberGenerator generator;
	private IPercentileCalculator calculator;
	
	public DistributionTester(INumberGenerator generator, IPercentileCalculator calculator) {
		this.generator = generator;
		this.calculator = calculator;
	}
	
	public void run() {
		List<Integer> numbers = generator.generate();
		Collections.sort(numbers);
		System.out.println("Numbers: " + Arrays.toString(numbers.toArray()));
		for(int i = 1; i < 10; i++) {
			int p = i*10;
			System.out.println(p + ". percentile is " + calculator.calculate(numbers, p));
		}
	}
	
	public static void main(String[] args) {
		DistributionTester dt =  new DistributionTester(new NumberGeneratorGauss(5, 10, 3), new PercentileCalculatorInterpolation());
		dt.run();
	}
}
