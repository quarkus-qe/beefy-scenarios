package io.quarkus.qe.containers;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class KafkaTestResource implements QuarkusTestResourceLifecycleManager {

    private KafkaContainer container;

    @Override
    public Map<String, String> start() {
        container = new KafkaContainer();
        container.start();

        final String hosts = container.getContainerIpAddress() + ":" + container.getMappedPort(KafkaContainer.KAFKA_PORT);

        return Collections.singletonMap("kafka.bootstrap.servers", hosts);
    }

    @Override
    public void stop() {
        Optional.ofNullable(container).ifPresent(GenericContainer::stop);
    }

}
