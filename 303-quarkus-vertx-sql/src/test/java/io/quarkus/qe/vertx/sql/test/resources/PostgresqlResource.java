package io.quarkus.qe.vertx.sql.test.resources;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class PostgresqlResource implements QuarkusTestResourceLifecycleManager {

    private GenericContainer<?> postgresContainer;

    @Override
    public Map<String, String> start() {
        postgresContainer = new GenericContainer<>(DockerImageName.parse("quay.io/bitnami/postgresql:13.4.0"))
                .withEnv("POSTGRES_USER", "test")
                .withEnv("POSTGRES_PASSWORD", "test")
                .withEnv("POSTGRES_DB", "amadeus")
                .withExposedPorts(5432);

        postgresContainer.waitingFor(new HostPortWaitStrategy()).waitingFor(
                Wait.forLogMessage(".*listening on IPv6.*", 1)
                        .withStartupTimeout(Duration.ofMinutes(3)))
                .start();

        Map<String, String> config = new HashMap<>();
        config.put("quarkus.datasource.jdbc.url", String.format("jdbc:postgresql://%s:%d/amadeus", postgresContainer.getHost(),
                postgresContainer.getFirstMappedPort()));
        config.put("quarkus.datasource.reactive.url", String.format("postgresql://%s:%d/amadeus", postgresContainer.getHost(),
                postgresContainer.getFirstMappedPort()));
        config.put("app.selected.db", "postgresql");
        // Enable Flyway for Postgresql
        config.put("quarkus.flyway.migrate-at-start", "true");
        // Disable Flyway for MySQL
        config.put("quarkus.flyway.mysql.migrate-at-start", "false");
        // Disable Flyway for DB2
        config.put("quarkus.flyway.db2.migrate-at-start", "false");

        return config;
    }

    @Override
    public void stop() {
        if (postgresContainer != null) {
            postgresContainer.stop();
        }
    }
}
