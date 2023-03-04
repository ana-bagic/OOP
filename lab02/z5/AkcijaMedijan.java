package lab2.z5;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AkcijaMedijan implements Akcija {

	@Override
	public void notify(List<Integer> numbers) {
		System.out.print("Medijan: ");
		List<Integer> copy = new LinkedList<>(numbers);
		Collections.sort(copy);
		int N = numbers.size();
		if(N%2 == 0) {
			int low = copy.get(N/2);
			int high = copy.get(N/2 - 1);
			System.out.println(low + ((high - low)/2.0));
		} else {
			System.out.println(copy.get(N/2));
		}
	}

}
