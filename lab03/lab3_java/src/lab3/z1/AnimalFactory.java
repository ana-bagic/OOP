package lab3.z1;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import lab3.z1.model.Animal;

public class AnimalFactory {

	@SuppressWarnings("unchecked")
	public static Animal newInstance(String animalKind, String name)
		throws InstantiationException, IllegalAccessException, IllegalArgumentException,
		InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException {
		
		Class<Animal> animalClass = (Class<Animal>)Class.forName("lab3.z1.model.plugins." + animalKind);
		Constructor<?> ctr = animalClass.getConstructor(String.class);
		return (Animal) ctr.newInstance(name);
	}
}
