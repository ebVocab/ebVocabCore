package de.ebuchner.vocab.config;

import java.io.InputStream;
import java.util.Properties;

public class Keys {

    private static final String CONFIG_REF = "keys.properties";
    private static Properties properties;

    private Keys() {

    }

    public static String getKey(String keyName) {
        return properties().getProperty(keyName);
    }

    private static Properties properties() {
        if (properties != null)
            return properties;
        try {
            InputStream in = Keys.class.getResourceAsStream(CONFIG_REF);
            try {
                properties = new Properties();
                properties.load(in);
                return properties;
            } finally {
                in.close();
            }
        } catch (Exception e) {
            throw new KeyAccessException(e);
        }
    }

    public static class KeyAccessException extends RuntimeException {
        public KeyAccessException(Exception source) {
            super("Error accessing " + CONFIG_REF + ": " + source.getMessage());
        }
    }
}
