package lab2.z4;

import java.util.List;

public class PercentileCalculatorInterpolation implements IPercentileCalculator {

	@Override
	public int calculate(List<Integer> numbers, int percentile) {
		if(numbers == null || numbers.size() == 0) {
			throw new IllegalArgumentException("There has to be at least one number in list.");
		}
		if(percentile < 1 || percentile > 100) {
			throw new IllegalArgumentException("Percentile has to be in interval <0, 100]");
		}
		
		int lowIndex = 0;
		int N = numbers.size();
		
		while(lowIndex < N) {
			double pvi = 100*(lowIndex + 0.5)/N;
			if(pvi > percentile) break;
			lowIndex++;
		}
		
		if(lowIndex == 0) return numbers.get(0);
		if(lowIndex == N) return numbers.get(N-1);
		
		double pvi = 100*(lowIndex - 0.5)/N;
		int vi = numbers.get(lowIndex-1);
		int vi1 = numbers.get(lowIndex);
		return (int) (vi + N*(percentile-pvi)*(vi1-vi)/100);
	}

}
