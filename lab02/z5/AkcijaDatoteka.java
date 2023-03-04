package lab2.z5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AkcijaDatoteka implements Akcija {

	private String path;
	
	public AkcijaDatoteka(String path) {
		this.path = path;
	}
	
	@Override
	public void notify(List<Integer> numbers) {
		try(FileOutputStream os = new FileOutputStream(new File(path))) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			String date = new String(sdf.format(new Date()) + "\n");
			os.write(date.getBytes());
			for(int n: numbers) os.write(new String(n + " ").getBytes());
		} catch (IOException e) {}
	}

}
