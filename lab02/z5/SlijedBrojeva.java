package lab2.z5;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SlijedBrojeva {

	private Izvor generator;
	private List<Integer> numbers = new LinkedList<>();
	private List<Akcija> actions = new LinkedList<>();
	
	public SlijedBrojeva(Izvor generator) {
		this.generator = Objects.requireNonNull(generator);
	}
	
	public void setGenerator(Izvor generator) {
		this.generator = generator;
	}
	
	public void addAction(Akcija action) {
		actions.add(action);
	}
	
	public void kreni() {
		while(true) {
			int num = generator.generate();
			if(num == -1) return;
			numbers.add(num);
			actions.forEach(a -> a.notify(numbers));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}	
	}

	public static void main(String[] args) throws FileNotFoundException {
		SlijedBrojeva sb = new SlijedBrojeva(new DatotecniIzvor("z5in.txt"));
		sb.addAction(new AkcijaMedijan());
		sb.kreni();
	}
}
