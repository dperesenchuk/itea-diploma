package ua.itea.diploma;

import java.io.IOException;
import java.util.Properties;

public class ApplicationProperties {
    private final Properties properties;

    ApplicationProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));

        } catch (IOException e) {
            System.out.println("IOException while loading properties file::::" + e.getMessage());
        }
    }

    public String readProperty(String keyName) {
        System.out.println("Reading Property " + keyName);
        return properties.getProperty(keyName, "There is no key in the properties file");
    }
}
