package lab2.z4;

import java.util.List;

public class PercentileCalculatorNearestRank implements IPercentileCalculator {

	@Override
	public int calculate(List<Integer> numbers, int percentile) {
		if(numbers == null || numbers.size() == 0) {
			throw new IllegalArgumentException("There has to be at least one number in list.");
		}
		if(percentile < 1 || percentile > 100) {
			throw new IllegalArgumentException("Percentile has to be in interval <0, 100]");
		}
		if(percentile == 100) return numbers.get(numbers.size()-1);
		
		int nP = (int)Math.ceil((percentile/100.0f)*numbers.size());
		return numbers.get(nP-1);
	}

}
