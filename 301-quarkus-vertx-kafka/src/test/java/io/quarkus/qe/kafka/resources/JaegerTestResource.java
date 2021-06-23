package io.quarkus.qe.kafka.resources;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.testcontainers.containers.GenericContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class JaegerTestResource implements QuarkusTestResourceLifecycleManager {

    private GenericContainer<?> container;

    @Override
    public Map<String, String> start() {
        container = new JaegerContainer();
        container.start();

        return Collections.singletonMap(
                "quarkus.opentelemetry.tracer.exporter.jaeger.endpoint",
                String.format("http://%s:%s/api/traces", container.getContainerIpAddress(), JaegerContainer.TRACE_PORT));
    }

    @Override
    public void stop() {
        Optional.ofNullable(container).ifPresent(GenericContainer::stop);
    }
}
