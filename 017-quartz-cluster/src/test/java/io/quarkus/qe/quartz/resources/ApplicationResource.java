package io.quarkus.qe.quartz.resources;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import io.quarkus.test.QuarkusProdModeTest;

public abstract class ApplicationResource extends QuarkusProdModeTest {

    private static final String RESOURCES_FOLDER = "src/main/resources/";
    private static final String FLYWAY_FOLDER = "db/migration/";

    private static final String COMMON_PROPERTIES = "common.properties";

    private static final String FLYWAY_ENTITIES_SQL = FLYWAY_FOLDER + "V1.0.0__init.sql";
    private static final String FLYWAY_QUARTZ_SQL = FLYWAY_FOLDER + "V2.0.0__QuarkusQuartz.sql";

    public ApplicationResource(Class<?>[] classes, Map<String, String> customProps) {
        JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class)
                .addClasses(classes);

        javaArchive.addAsResource(new File(RESOURCES_FOLDER + FLYWAY_ENTITIES_SQL), FLYWAY_ENTITIES_SQL);
        javaArchive.addAsResource(new File(RESOURCES_FOLDER + FLYWAY_QUARTZ_SQL), FLYWAY_QUARTZ_SQL);

        setArchiveProducer(() -> javaArchive);
        setRun(true);

        Map<String, String> properties = new HashMap<>(toMap(COMMON_PROPERTIES));
        properties.putAll(customProps);

        setRuntimeProperties(properties);
    }

    protected static final Class<?>[] classes(Class<?>... classes) {
        return classes;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Map<String, String> toMap(String propertiesFile) {
        Properties properties = new Properties();
        try (InputStream in = ClassLoader.getSystemResourceAsStream(propertiesFile)) {
            properties.load(in);
        } catch (IOException e) {
            fail("Could not start extension. Caused by " + e);
        }

        return (Map) properties;
    }
}
