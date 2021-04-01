package quarkus.qe.providers;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.microprofile.config.spi.ConfigSource;

public class CustomConfigSource implements ConfigSource {

    private static final int ORDINAL = 999;
    private static final String PROPERTIES_FILE = "/configsource.properties";

    Properties customProperties = new Properties();

    @Override
    public Map<String, String> getProperties() {
        loadProperties();
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : customProperties.entrySet()) {
            result.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return result;
    }

    @Override
    public int getOrdinal() {
        return ORDINAL;
    }

    @Override
    public String getValue(String propertyName) {
        loadProperties();
        return customProperties.getProperty(propertyName);
    }

    @Override
    public String getName() {
        return "Custom Config Source";
    }

    public void loadProperties(){
        try {
            InputStream in = CustomConfigSource.class.getResourceAsStream(PROPERTIES_FILE);
            if (in != null) {
                customProperties.load(in);
            } else {
                throw new FileNotFoundException("Property file " + PROPERTIES_FILE + " not found in the classpath");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
