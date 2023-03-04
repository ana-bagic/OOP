package lab2.z5;

import java.util.Scanner;

public class TipkovnickiIzvor implements Izvor {

	private Scanner sc = new Scanner(System.in);
	
	@Override
	public int generate() {
		while(true) {
			String gen = sc.nextLine();
			try {
				int ret = Integer.parseInt(gen);
				if(ret >= 0) return ret;
			} catch (NumberFormatException e) {
				if(gen.toLowerCase().equals("done")) {
					sc.close();
					return -1;
				}
			}
			System.out.println("Write not negative integer or \"done\" if you are done.");
		}
	}

}
