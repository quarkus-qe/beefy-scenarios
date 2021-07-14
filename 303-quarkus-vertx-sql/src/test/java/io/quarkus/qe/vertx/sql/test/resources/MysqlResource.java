package io.quarkus.qe.vertx.sql.test.resources;

import java.util.HashMap;
import java.util.Map;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class MysqlResource implements QuarkusTestResourceLifecycleManager {

    private GenericContainer<?> mysqlContainer;

    @Override
    public Map<String, String> start() {
        mysqlContainer = new GenericContainer<>(DockerImageName.parse("quay.io/bitnami/mysql:5.7.32"))
                .withEnv("MYSQL_ROOT_PASSWORD", "test")
                .withEnv("MYSQL_USER", "test")
                .withEnv("MYSQL_PASSWORD", "test")
                .withEnv("MYSQL_DATABASE", "amadeus")
                .withExposedPorts(3306);

        mysqlContainer.waitingFor(new HostPortWaitStrategy()).waitingFor(
                Wait.forLogMessage(".*MySQL Community Server.*", 1)).start();

        Map<String, String> config = new HashMap<>();
        config.put("quarkus.datasource.mysql.jdbc.url",
                String.format("jdbc:mysql://%s:%d/amadeus", mysqlContainer.getHost(), mysqlContainer.getFirstMappedPort()));
        config.put("quarkus.datasource.mysql.reactive.url",
                String.format("mysql://%s:%d/amadeus", mysqlContainer.getHost(), mysqlContainer.getFirstMappedPort()));
        config.put("app.selected.db", "mysql");
        // Enable Flyway for MySQL
        config.put("quarkus.flyway.mysql.migrate-at-start", "true");
        // Disable Flyway for Postgresql
        config.put("quarkus.flyway.migrate-at-start", "false");
        // Disable Flyway for DB2
        config.put("quarkus.flyway.db2.migrate-at-start", "false");

        return config;
    }

    @Override
    public void stop() {
        if (mysqlContainer != null) {
            mysqlContainer.stop();
        }
    }
}