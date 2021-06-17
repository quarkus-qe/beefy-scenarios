package io.quarkus.qe.containers;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class JaegerTestResource implements QuarkusTestResourceLifecycleManager {

    public static final String JAEGER_API_ENDPOINT = "jaeger.api.endpoint";

    private static final String JAEGER_ENDPOINT = "http://%s:%s/api/traces";
    private static final int TRACE_PORT = 14268;
    private static final int REST_PORT = 16686;
    private static final String QUARKUS_JAEGER_PROPERTY = "quarkus.jaeger.endpoint";

    private GenericContainer<?> container;

    @Override
    public Map<String, String> start() {

        container = new GenericContainer<>("jaegertracing/all-in-one:latest")
                .waitingFor(
                        new LogMessageWaitStrategy().withRegEx(".*\"Health Check state change\",\"status\":\"ready\".*\\s"));
        container.start();

        return Collections.singletonMap(QUARKUS_JAEGER_PROPERTY, traceEndpoint());
    }

    @Override
    public void stop() {
        Optional.ofNullable(container).ifPresent(GenericContainer::stop);
    }

    @Override
    public void inject(Object testInstance) {
        Class<?> c = testInstance.getClass();
        while (c != Object.class) {
            for (Field f : c.getDeclaredFields()) {
                ConfigProperty configProperty = f.getAnnotation(ConfigProperty.class);
                if (configProperty != null && JAEGER_API_ENDPOINT.equals(configProperty.name())) {
                    setFieldValue(f, testInstance, apiEndpoint());
                }
            }
            c = c.getSuperclass();
        }
    }

    private String traceEndpoint() {
        return String.format(JAEGER_ENDPOINT, container.getContainerIpAddress(), container.getMappedPort(TRACE_PORT));
    }

    private String apiEndpoint() {
        return String.format(JAEGER_ENDPOINT, container.getContainerIpAddress(), container.getMappedPort(REST_PORT));
    }

    private void setFieldValue(Field f, Object testInstance, Object value) {
        try {
            f.setAccessible(true);
            f.set(testInstance, value);
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
