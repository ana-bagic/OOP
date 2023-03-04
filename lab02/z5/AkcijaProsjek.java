package lab2.z5;

import java.util.List;

public class AkcijaProsjek implements Akcija {

	@Override
	public void notify(List<Integer> numbers) {
		System.out.print("Prosjek: ");
		System.out.println(numbers.stream().mapToInt(i -> i).average().getAsDouble());
	}

}
