package io.quarkus.qe.multiplepus.containers;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class PostgreSqlDatabaseTestResource implements QuarkusTestResourceLifecycleManager {

    private static final PostgreSQLContainer<?> DATABASE = new PostgreSQLContainer<>("postgres:13.1")
            .withDatabaseName("postgresql")
            .withUsername("postgresql")
            .withPassword("postgresql");

    @Override
    public Map<String, String> start() {
        DATABASE.start();

        return Collections.singletonMap("quarkus.datasource.\"vegetables\".jdbc.url", DATABASE.getJdbcUrl());
    }

    @Override
    public void stop() {
        Optional.ofNullable(DATABASE).ifPresent(GenericContainer::stop);
    }
}
