package io.quarkus.qe.kafka.resources;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.jboss.logging.Logger;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.lifecycle.Startables;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import org.testcontainers.utility.DockerImageName;

public class ConfluentKafkaResource implements QuarkusTestResourceLifecycleManager {

    private static final Logger LOG = Logger.getLogger(ConfluentKafkaResource.class);

    private KafkaContainer kafkaContainer;
    private SchemaRegistryContainer schemaRegistry;

    @Override
    public Map<String, String> start() {
        Network network = Network.newNetwork();
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.3.0")).withNetwork(network);
        schemaRegistry = new SchemaRegistryContainer("confluentinc/cp-schema-registry","5.3.0", 8081).withNetwork(network).withKafka(kafkaContainer, 9092);

        Startables.deepStart(Stream.of(kafkaContainer, schemaRegistry)).join();

        String kafkaUrl = kafkaContainer.getBootstrapServers();
        String registryUrl = schemaRegistry.getSchemaRegistryUrl();

        LOG.info(String.format("TestContainers Kafka URL -> %s", kafkaUrl));
        LOG.info(String.format("TestContainers Registry URL -> %s", registryUrl));

        Map<String, String> config = new HashMap<>();
        config.put("kafka.bootstrap.servers", kafkaUrl);
        config.put("mp.messaging.connector.smallrye-kafka.schema.registry.url", registryUrl);
        config.put("mp.messaging.outgoing.source-stock-price.schema.registry.url", registryUrl);
        config.put("mp.messaging.incoming.channel-stock-price.schema.registry.url", registryUrl);
        config.put("mp.messaging.outgoing.sink-stock-price.schema.registry.url", registryUrl);

        return config;
    }

    @Override
    public void stop() {
        if (Objects.nonNull(kafkaContainer)) kafkaContainer.stop();
        if(Objects.nonNull(schemaRegistry)) schemaRegistry.stop();
    }
}
