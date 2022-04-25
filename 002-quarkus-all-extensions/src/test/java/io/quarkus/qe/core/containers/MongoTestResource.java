package io.quarkus.qe.core.containers;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class MongoTestResource implements QuarkusTestResourceLifecycleManager {

    private static final String QUARKUS_MONGODB_HOST = "quarkus.mongodb.connection-string";
    private static final String MONGODB_DATABASE_NAME_DEFAULT = "test";
    private static final int PORT = 27017;

    private GenericContainer<?> resource;

    @Override
    public Map<String, String> start() {
        resource = new GenericContainer<>("docker.io/mongo:4.0.23");
        resource.waitingFor(new LogMessageWaitStrategy().withRegEx("(?i).*waiting for connections.*"));
        resource.addExposedPort(PORT);
        resource.start();
        return Collections.singletonMap(QUARKUS_MONGODB_HOST, getReplicaSetUrl());
    }

    @Override
    public void stop() {
        Optional.ofNullable(resource).ifPresent(GenericContainer::stop);
    }

    private String getReplicaSetUrl() {
        return String.format(
                "mongodb://%s:%d/%s",
                resource.getContainerIpAddress(),
                resource.getMappedPort(PORT),
                MONGODB_DATABASE_NAME_DEFAULT);
    }
}
