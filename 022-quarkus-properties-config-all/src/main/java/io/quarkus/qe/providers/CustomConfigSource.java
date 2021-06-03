package io.quarkus.qe.providers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.spi.ConfigSource;

public class CustomConfigSource implements ConfigSource {

    private static final int ORDINAL = 999;
    private static final String PROPERTIES_FILE = "/configsource.properties";

    private final Properties customProperties = new Properties();

    public CustomConfigSource() throws IOException {
        loadProperties();
    }

    @Override
    public Set<String> getPropertyNames() {
        return customProperties.keySet().stream().map(Object::toString).collect(Collectors.toSet());
    }

    @Override
    public int getOrdinal() {
        return ORDINAL;
    }

    @Override
    public String getValue(String propertyName) {
        return customProperties.getProperty(propertyName);
    }

    @Override
    public String getName() {
        return "Custom Config Source";
    }

    public void loadProperties() throws IOException {
        InputStream in = CustomConfigSource.class.getResourceAsStream(PROPERTIES_FILE);
        if (in != null) {
            customProperties.load(in);
        } else {
            throw new FileNotFoundException("Property file " + PROPERTIES_FILE + " not found in the classpath");
        }
    }
}
