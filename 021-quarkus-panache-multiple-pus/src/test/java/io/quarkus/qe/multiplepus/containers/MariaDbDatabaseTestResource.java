package io.quarkus.qe.multiplepus.containers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MariaDBContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class MariaDbDatabaseTestResource implements QuarkusTestResourceLifecycleManager {

    private static final MariaDBContainer<?> DATABASE = new MariaDBContainer<>()
            .withDatabaseName("mariadb")
            .withUsername("mariadb")
            .withPassword("mariadb");

    @Override
    public Map<String, String> start() {
        DATABASE.start();
        Map<String, String> config = new HashMap<>(4);

        config.put("quarkus.datasource.\"fruits\".username", DATABASE.getUsername());
        config.put("quarkus.datasource.\"fruits\".password", DATABASE.getPassword());
        config.put("quarkus.datasource.\"fruits\".jdbc.url", DATABASE.getJdbcUrl());
        return config;
    }

    @Override
    public void stop() {
        Optional.ofNullable(DATABASE).ifPresent(GenericContainer::stop);
    }
}
