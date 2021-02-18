package org.acme.spring.data.rest.containers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class PostgreSqlDatabaseTestResource implements QuarkusTestResourceLifecycleManager {

    private static final String QUARKUS_DB_KIND = "quarkus.datasource.db-kind";
    private static final String QUARKUS_DB_JDBC_URL = "quarkus.datasource.jdbc.url";
    private static final String QUARKUS_DB_USER = "quarkus.datasource.username";
    private static final String QUARKUS_DB_PASSWORD = "quarkus.datasource.password";
    private static final String POSTGRESQL = "postgresql";

    private PostgreSQLContainer<?> container;

    @Override
    public Map<String, String> start() {
        container = new PostgreSQLContainer<>("postgres:13.1");
        container.start();

        Map<String, String> config = new HashMap<>();
        config.put(QUARKUS_DB_KIND, POSTGRESQL);
        config.put(QUARKUS_DB_JDBC_URL, container.getJdbcUrl());
        config.put(QUARKUS_DB_USER, container.getUsername());
        config.put(QUARKUS_DB_PASSWORD, container.getPassword());

        return Collections.unmodifiableMap(config);
    }

    @Override
    public void stop() {
        Optional.ofNullable(container).ifPresent(GenericContainer::stop);
    }
}