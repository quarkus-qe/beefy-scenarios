package io.quarkus.qe.vertx.sql.test.resources;

import static io.quarkus.qe.vertx.sql.test.resources.PostgresqlTestProfile.PROFILE;

import java.util.HashMap;
import java.util.Map;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;
import org.testcontainers.utility.DockerImageName;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class PostgresqlResource implements QuarkusTestResourceLifecycleManager {

    private GenericContainer<?> postgresContainer;

    @Override
    public Map<String, String> start() {
        Map<String, String> config = new HashMap<>();
        String profile = System.getProperty("quarkus.test.profile");
        if(StringUtils.isEmpty(profile) || profile.equals(PROFILE)) defaultPostgresContainer(config);

        return config;
    }

    @SuppressWarnings("resource")
    private void defaultPostgresContainer(Map<String, String> config) {
        postgresContainer = new GenericContainer<>(DockerImageName.parse("quay.io/debezium/postgres:latest"))
                .withEnv("POSTGRES_USER", "test")
                .withEnv("POSTGRES_PASSWORD", "test")
                .withEnv("POSTGRES_DB", "amadeus")
                .withExposedPorts(5432);

        postgresContainer.waitingFor(new HostPortWaitStrategy()).start();

        config.put("quarkus.datasource.jdbc.url", String.format("jdbc:postgresql://%s:%d/amadeus", postgresContainer.getHost(), postgresContainer.getFirstMappedPort()));
        config.put("quarkus.datasource.reactive.url", String.format("postgresql://%s:%d/amadeus", postgresContainer.getHost(), postgresContainer.getFirstMappedPort()));
        config.put("app.selected.db","postgresql");
        config.put("quarkus.flyway.mysql.migrate-at-start","false");
        config.put("quarkus.flyway.db2.migrate-at-start","false");
    }

    @Override
    public void stop() {
        if (postgresContainer != null) {
            postgresContainer.stop();
        }
    }
}
