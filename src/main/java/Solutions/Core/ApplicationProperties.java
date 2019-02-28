package Solutions.Core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {

    private static ApplicationProperties instance;

    private Properties properties;

    private ApplicationProperties() throws IOException {
        properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("application.properties");
        properties.load(stream);
    }

    public static void init() throws IOException {
        instance = new ApplicationProperties();
    }

    public static ApplicationProperties getInstance() {
        if (instance == null)
            throw new RuntimeException("Init method is never called.");
        return instance;
    }

    public String getProperty(String property) {
        return properties.getProperty(property);
    }
}
