package edu.pitt.sis.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configer {
    public static final String CONFIG = "../../property/config.properties";
    public static Properties PROP = Configer.readProperty(CONFIG);
    
    public static Properties readProperty(String fileName) {
        Properties prop = new Properties();
	InputStream input = null;
        
	try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            input = classLoader.getResourceAsStream(fileName);
            prop.load(input);
	} catch (IOException ex) {
            ex.printStackTrace();
	} finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
	}
        return prop;
    }
}
