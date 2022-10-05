package io.quarkus.qe.vertx.webclient.resources;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

public class JaegerContainer extends GenericContainer<JaegerContainer> {
    private static final String COLLECTOR_OTLP_ENABLED = "COLLECTOR_OTLP_ENABLED";
    private static final int OLTP_PORT = 4317;
    private static final int TRACE_PORT = 16686;

    public JaegerContainer() {
        super("quay.io/jaegertracing/all-in-one:1.37.0");
        waitingFor(new LogMessageWaitStrategy().withRegEx(".*\"Health Check state change\",\"status\":\"ready\".*\\s"));
        addFixedExposedPort(OLTP_PORT, OLTP_PORT);
        addFixedExposedPort(TRACE_PORT, TRACE_PORT);
        addEnv(COLLECTOR_OTLP_ENABLED, "true");
    }
}
