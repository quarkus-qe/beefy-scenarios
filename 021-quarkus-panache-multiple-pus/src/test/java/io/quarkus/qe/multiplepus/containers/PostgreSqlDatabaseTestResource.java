package io.quarkus.qe.multiplepus.containers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class PostgreSqlDatabaseTestResource implements QuarkusTestResourceLifecycleManager {

    private static final PostgreSQLContainer<?> DATABASE = new PostgreSQLContainer<>("postgres:13.1")
            .withDatabaseName("postgresql")
            .withUsername("postgresql")
            .withPassword("postgresql");

    @Override
    public Map<String, String> start() {
        DATABASE.start();
        Map<String, String> config = new HashMap<>(4);

        config.put("quarkus.datasource.\"vegetables\".username", DATABASE.getUsername());
        config.put("quarkus.datasource.\"vegetables\".password", DATABASE.getPassword());
        config.put("quarkus.datasource.\"vegetables\".jdbc.url", DATABASE.getJdbcUrl());
        return config;
    }

    @Override
    public void stop() {
        Optional.ofNullable(DATABASE).ifPresent(GenericContainer::stop);
    }
}
