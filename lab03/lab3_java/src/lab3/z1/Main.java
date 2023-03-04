package lab3.z1;

import lab3.z1.model.plugins.Parrot;

public class Main {

	public static void main(String[] args) {
		try {
			Parrot parrot = (Parrot) AnimalFactory.newInstance("Parrot", "Amadeus");
			parrot.animalPrintGreeting();
			parrot.animalPrintMenu();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
