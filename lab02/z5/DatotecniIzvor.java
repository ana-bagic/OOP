package lab2.z5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DatotecniIzvor implements Izvor {

	private Scanner sc;
	
	public DatotecniIzvor(String path) throws FileNotFoundException {
		sc = new Scanner(new File(path));
	}
	
	@Override
	public int generate() {
		while(sc.hasNextLine()) {
			String gen = sc.next();
			try {
				int ret = Integer.parseInt(gen);
				if(ret >= 0) return ret;
			} catch (NumberFormatException e) {}
		}
		sc.close();
		return -1;
	}

}
