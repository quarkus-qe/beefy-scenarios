package io.quarkus.qe.containers;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class ConsulTestResource implements QuarkusTestResourceLifecycleManager {
    private GenericContainer<?> resource;

    @SuppressWarnings("resource")
    @Override
    public Map<String, String> start() {

        resource = new GenericContainer<>("consul:1.7")
                .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Node info in sync.*\\s"))
                .withStartupTimeout(Duration.ofMillis(20000))
                .withNetworkMode("host")
                .withCommand("agent -dev");

        resource.start();

        Consul client = Consul.builder().build();
        KeyValueClient kvClient = client.keyValueClient();
        try {
            String properties = IOUtils
                    .toString(this.getClass().getClassLoader().getResourceAsStream("application.properties"),
                            StandardCharsets.UTF_8);
            kvClient.putValue("config/app", properties);
        } catch (IOException e) {
            fail("Failed to load properties. Caused by " + e.getMessage());
        }

        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        Optional.ofNullable(resource).ifPresent(GenericContainer::stop);
    }
}
