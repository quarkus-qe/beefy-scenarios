package io.quarkus.qe.vertx.sql.test.resources;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class Db2Resource implements QuarkusTestResourceLifecycleManager {

    private GenericContainer<?> db2Container;

    @Override
    public Map<String, String> start() {
        db2Container = new GenericContainer<>(DockerImageName.parse("icr.io/db2_community/db2:11.5.8.0"))
                .withPrivilegedMode(true)
                .withEnv("LICENSE", "accept")
                .withEnv("DB2INST1_PASSWORD", "test")
                .withEnv("DB2INSTANCE", "test")
                .withEnv("AUTOCONFIG", "false")
                .withEnv("ARCHIVE_LOGS", "false")
                .withEnv("DBNAME", "amadeus")
                .withExposedPorts(50000);

        db2Container.waitingFor(new HostPortWaitStrategy()).waitingFor(
                Wait.forLogMessage(".*Setup has completed\\..*", 1).withStartupTimeout(Duration.ofMinutes(10))).start();

        Map<String, String> config = new HashMap<>();
        config.put("quarkus.datasource.db2.jdbc.url",
                String.format("jdbc:db2://%s:%d/amadeus", db2Container.getHost(), db2Container.getFirstMappedPort()));
        config.put("quarkus.datasource.db2.reactive.url",
                String.format("db2://%s:%d/amadeus", db2Container.getHost(), db2Container.getFirstMappedPort()));
        config.put("app.selected.db", "db2");
        // Enable Flyway for DB2
        config.put("quarkus.flyway.db2.migrate-at-start", "true");
        // Disable Flyway for Postgresql
        config.put("quarkus.flyway.migrate-at-start", "false");
        // Disable Flyway for MySQL
        config.put("quarkus.flyway.mysql.migrate-at-start", "false");

        return config;
    }

    @Override
    public void stop() {
        if (db2Container != null) {
            db2Container.stop();
        }
    }

}
