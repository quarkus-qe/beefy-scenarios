package io.quarkus.qe.kafka.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.jboss.logging.Logger;
import org.testcontainers.containers.Network;
import org.testcontainers.lifecycle.Startables;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.strimzi.StrimziKafkaContainer;

public class StrimziKafkaResource implements QuarkusTestResourceLifecycleManager {

    private static final Logger LOG = Logger.getLogger(ConfluentKafkaResource.class);

    private StrimziKafkaContainer kafkaContainer;
    private SchemaRegistryContainer schemaRegistry;

    @Override
    public Map<String, String> start() {
        Network network = Network.newNetwork();

        kafkaContainer = new StrimziKafkaContainer("latest-kafka-2.7.0").withNetwork(network);
        schemaRegistry = new SchemaRegistryContainer("apicurio/apicurio-registry-mem", "1.2.2.Final", 8080).withNetwork(network)
                .withKafka(kafkaContainer, 9092);

        Startables.deepStart(Stream.of(kafkaContainer, schemaRegistry)).join();

        String kafkaUrl = kafkaContainer.getBootstrapServers();
        String registryUrl = schemaRegistry.getSchemaRegistryUrl();

        LOG.info(String.format("TestContainers Kafka URL -> %s", kafkaUrl));
        LOG.info(String.format("TestContainers Registry URL -> %s", registryUrl));

        Map<String, String> config = new HashMap<>();
        config.put("kafka.bootstrap.servers", kafkaUrl);
        config.put("quarkus.kafka-streams.bootstrap-servers", kafkaUrl);
        config.put("mp.messaging.connector.smallrye-kafka.apicurio.registry.url", registryUrl + "/api");

        return config;
    }

    @Override
    public void stop() {
        if (Objects.nonNull(kafkaContainer))
            kafkaContainer.stop();
        if (Objects.nonNull(schemaRegistry))
            schemaRegistry.stop();
    }
}
