package io.github.yashchenkon.banking.infra.properties;

import java.util.Properties;

public class DatabaseProperties {
    private static final String URL_PROPERTY = "database.url";
    private static final String USERNAME_PROPERTY = "database.username";
    private static final String PASSWORD_PROPERTY = "database.password";

    public final Properties applicationProperties;

    public DatabaseProperties(Properties properties) {
        this.applicationProperties = properties;
    }

    public String url() {
        return applicationProperties.getProperty(URL_PROPERTY);
    }

    public String username() {
        return applicationProperties.getProperty(USERNAME_PROPERTY);
    }

    public String password() {
        return applicationProperties.getProperty(PASSWORD_PROPERTY);
    }
}
