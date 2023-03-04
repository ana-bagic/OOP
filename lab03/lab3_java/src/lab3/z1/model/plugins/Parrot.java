package lab3.z1.model.plugins;

import lab3.z1.model.Animal;

public class Parrot extends Animal {

	private String name;
	
	public Parrot(String name) {
		this.name = name;
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public String greet() {
		return "sqwak!";
	}

	@Override
	public String menu() {
		return "sjemenke";
	}

}
