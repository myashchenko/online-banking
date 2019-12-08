package io.github.yashchenkon.banking.infra.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertiesLoader {

    public static ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();

    /**
     * Loads properties from the file located in classpath.
     *
     * @param path relative pass to a file in classpath
     * @return properties
     */
    public Properties load(String path) {

        Properties properties = new Properties();
        try (InputStream resourceStream = CLASS_LOADER.getResourceAsStream(path)) {
            Objects.requireNonNull(resourceStream);

            properties.load(resourceStream);
        } catch (IOException e) {
            throw new RuntimeException("Exception happened while loading properties file", e);
        }

        return properties;
    }
}
