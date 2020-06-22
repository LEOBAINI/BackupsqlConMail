package configs;

import java.io.IOException;
import java.util.Properties;

public class ReadPropertyFile {
	
	private static ReadPropertyFile readPropertyFile;
	
			
	
	public static ReadPropertyFile getInstance() {
		if(readPropertyFile==null) {
			readPropertyFile=new ReadPropertyFile();
		}
		return readPropertyFile;
	}
	public Properties obtenerPropiedades() {
		Properties prop=new Properties();
		try {
			prop.load(ReadPropertyFile.class.getResourceAsStream("Configs.properties"));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		/*FileInputStream ip = null;
		try {
			ip = new FileInputStream("src/main/resources/Configs.properties");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			prop.load(ip);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return prop;
	}
	

}
