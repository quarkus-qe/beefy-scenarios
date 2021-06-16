package io.quarkus.qe.containers;

import java.nio.charset.StandardCharsets;

import org.testcontainers.containers.InternetProtocol;
import org.testcontainers.containers.Network;
import org.testcontainers.images.builder.Transferable;

import com.github.dockerjava.api.command.InspectContainerResponse;

public class SaslStrimziKafkaContainer extends KafkaContainerHelper<SaslStrimziKafkaContainer> {

    public SaslStrimziKafkaContainer(final String version) {
        super("quay.io/strimzi/kafka:" + version);
        super.withNetwork(Network.SHARED);

        // exposing kafka port from the container
        withExposedPorts(KAFKA_PORT);
        addFixedExposedPort(KAFKA_PORT, KAFKA_PORT, InternetProtocol.TCP);

        withEnv("LOG_DIR", "/tmp");
    }

    public SaslStrimziKafkaContainer() {
        this("latest-kafka-" + LATEST_KAFKA_VERSION);
    }

    @Override
    protected void containerIsStarting(InspectContainerResponse containerInfo, boolean reused) {
        super.containerIsStarting(containerInfo, reused);

        String command = "#!/bin/bash \n";
        command += "bin/zookeeper-server-start.sh config/zookeeper.properties &\n";
        command += "bin/kafka-server-start.sh config/server.properties" +
                " --override listeners=SASL_PLAINTEXT://:" + KAFKA_PORT +
                " --override advertised.listeners=" + getBootstrapServers() +
                " --override zookeeper.connect=localhost:" + ZOOKEEPER_PORT;

        copyFileToContainer(
                Transferable.of(command.getBytes(StandardCharsets.UTF_8), 700),
                STARTER_SCRIPT);
    }

    public String getBootstrapServers() {
        return String.format("SASL_PLAINTEXT://%s:%s", getContainerIpAddress(), KAFKA_PORT);
    }
}
