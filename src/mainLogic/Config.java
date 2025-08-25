package mainLogic;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Properties properties;
    
    static {
        properties = new Properties();
        try (InputStream input = new FileInputStream("resources/config.properties")) {
            properties.load(input);
        } catch (Exception e) {
        	System.out.println("Файл конфига не найден");
        }
    }
    
    public static String getApiUrl() {
        return properties.getProperty("api.url");
    }
    
    public static String getApiKey() {
        return properties.getProperty("api.key");
    }
}