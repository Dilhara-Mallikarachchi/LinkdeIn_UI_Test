package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in != null) props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load config.properties", e);
        }
    }

    // Environment variables (LINKEDIN_USERNAME) override the file
    public static String get(String key) {
        String env = System.getenv(key.toUpperCase().replace('.', '_'));
        return env != null ? env : props.getProperty(key);
    }
}