package io.quarkus.qe.kafka.resources;

import io.strimzi.StrimziKafkaContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;

public class SchemaRegistryContainer extends GenericContainer<SchemaRegistryContainer> {

    private final String image;
    private final String version;
    private final int port;

    public SchemaRegistryContainer(final String image, final String version, final int port) {
        super(image + ":"+ version);
        this.image = image;
        this.version = version;
        this.port = port;

        withExposedPorts(port);
    }

    public SchemaRegistryContainer withKafka(KafkaContainer kafka, int kPort) {
        return withConfluentKafka(kafka.getNetwork(), kafka.getNetworkAliases().get(0) + ":" + kPort);
    }

    public SchemaRegistryContainer withKafka(StrimziKafkaContainer kafka, int kPort) {
        return withStrimziKafka(kafka.getNetwork(), kafka.getNetworkAliases().get(0) + ":" + kPort);
    }

    protected SchemaRegistryContainer withStrimziKafka(Network network,String bootstrapServers) {
        withNetwork(network);
        withEnv("QUARKUS_PROFILE", "strimzi");
        withEnv("APPLICATION_ID", "registry_id");
        withEnv("KAFKA_BOOTSTRAP_SERVERS", bootstrapServers);
        withEnv("APPLICATION_SERVER", "localhost:9000");
        return self();
    }

    protected SchemaRegistryContainer withConfluentKafka(Network network, String bootstrapServers) {
        withNetwork(network);
        withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry");
        withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:" + port);
        withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS", "PLAINTEXT://" + bootstrapServers);
        return self();
    }


    /**
     * @return Schema Registry URL
     */
    public String getSchemaRegistryUrl() {
        return "http://" + getContainerIpAddress() + ":" + getMappedPort(port);
    }
}
