package io.quarkus.qe.containers;

import java.time.Duration;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

public class ConsulContainer extends GenericContainer<ConsulContainer> {
    private static final int PORT = 8500;

    public ConsulContainer() {
        super("docker.io/consul:1.9.3");
        waitingFor(new LogMessageWaitStrategy().withRegEx(".*Synced node info.*\\s"));
        withStartupTimeout(Duration.ofMillis(60000));
        addFixedExposedPort(PORT, PORT);
    }
}
