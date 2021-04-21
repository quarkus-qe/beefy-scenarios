package io.quarkus.qe.containers;

import java.time.Duration;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

public class ConsulContainer extends GenericContainer<ConsulContainer> {
    private static final int PORT = 8500;
    private static final int STARTUP_TIMEOUT_MILLIS = 60000;

    public ConsulContainer() {
        super("quay.io/bitnami/consul:1.9.3");
        waitingFor(new LogMessageWaitStrategy().withRegEx(".*Synced node info.*\\s"));
        withStartupTimeout(Duration.ofMillis(STARTUP_TIMEOUT_MILLIS));
        addFixedExposedPort(PORT, PORT);
    }
}
