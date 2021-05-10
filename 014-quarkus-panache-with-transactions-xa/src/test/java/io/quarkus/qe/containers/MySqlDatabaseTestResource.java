package io.quarkus.qe.containers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class MySqlDatabaseTestResource implements QuarkusTestResourceLifecycleManager {

    private static final String QUARKUS_DB_JDBC_URL = "quarkus.datasource%s.jdbc.url";
    private static final String QUARKUS_DB_USER = "quarkus.datasource%s.username";
    private static final String QUARKUS_DB_PASSWORD = "quarkus.datasource%s.password";
    private static final String DEFAULT_SCHEMA = "test";
    private static final String MYSQL = "mysql";
    private static final String WITH_XA_PROFILE = ".with-xa";

    private MySQLContainer<?> container;

    @Override
    public Map<String, String> start() {
        container = new MySQLContainer<>(
                DockerImageName.parse("quay.io/bitnami/mysql:5.7.32").asCompatibleSubstituteFor(MYSQL));
        container.withUrlParam("currentSchema", DEFAULT_SCHEMA);
        container.start();

        Map<String, String> config = new HashMap<>();
        config.put(defaultDataSource(QUARKUS_DB_JDBC_URL), container.getJdbcUrl());
        config.put(defaultDataSource(QUARKUS_DB_USER), container.getUsername());
        config.put(defaultDataSource(QUARKUS_DB_PASSWORD), container.getPassword());
        config.put(withXaDataSource(QUARKUS_DB_JDBC_URL), container.getJdbcUrl());
        config.put(withXaDataSource(QUARKUS_DB_USER), container.getUsername());
        config.put(withXaDataSource(QUARKUS_DB_PASSWORD), container.getPassword());

        return Collections.unmodifiableMap(config);
    }

    @Override
    public void stop() {
        Optional.ofNullable(container).ifPresent(GenericContainer::stop);
    }

    private String defaultDataSource(String key) {
        return String.format(key, StringUtils.EMPTY);
    }

    private String withXaDataSource(String key) {
        return String.format(key, WITH_XA_PROFILE);
    }
}
