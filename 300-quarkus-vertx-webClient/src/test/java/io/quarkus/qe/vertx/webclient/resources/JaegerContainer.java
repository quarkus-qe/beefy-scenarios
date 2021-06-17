package io.quarkus.qe.vertx.webclient.resources;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

public class JaegerContainer extends GenericContainer<JaegerContainer> {
    public static final int REST_PORT = 16686;
    private static final int TRACE_PORT = 14250;

    public JaegerContainer() {
        super("jaegertracing/all-in-one:latest");
        waitingFor(new LogMessageWaitStrategy().withRegEx(".*\"Health Check state change\",\"status\":\"ready\".*\\s"));
        addFixedExposedPort(REST_PORT, REST_PORT);
        addFixedExposedPort(TRACE_PORT, TRACE_PORT);
    }
}
