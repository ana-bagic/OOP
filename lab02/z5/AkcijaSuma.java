package lab2.z5;

import java.util.List;

public class AkcijaSuma implements Akcija {

	@Override
	public void notify(List<Integer> numbers) {
		System.out.print("Suma: ");
		System.out.println(numbers.stream().mapToInt(i -> i).sum());
	}

}
