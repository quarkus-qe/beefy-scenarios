package org.acme.quickstart.containers;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class InfinispanTestResource implements QuarkusTestResourceLifecycleManager {

    private static final Integer INFINISPAN_PORT = 11222;

    private GenericContainer<?> infinispan;

    @SuppressWarnings("resource")
    @Override
    public Map<String, String> start() {

        infinispan = new GenericContainer<>("infinispan/server:11.0.4.Final")
                .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Infinispan Server.*started in.*\\s"))
                .withStartupTimeout(Duration.ofMillis(20000))
                .withClasspathResourceMapping("identities.yaml",
                        "/user-config/identities.yaml", BindMode.READ_ONLY)
                .withClasspathResourceMapping("config.yaml",
                "/user-config/config.yaml", BindMode.READ_ONLY)
                .withClasspathResourceMapping("server.jks",
                       "/user-config/server.jks", BindMode.READ_ONLY)
                .withEnv("CONFIG_PATH", "/user-config/config.yaml")
                .withEnv("IDENTITIES_PATH", "/user-config/identities.yaml");

        infinispan.start();
        final String hosts = infinispan.getContainerIpAddress() + ":" + infinispan.getMappedPort(INFINISPAN_PORT);

        return Collections.singletonMap("quarkus.infinispan-client.server-list", hosts);
    }

    @Override
    public void stop() {
        Optional.ofNullable(infinispan).ifPresent(GenericContainer::stop);
    }
}
