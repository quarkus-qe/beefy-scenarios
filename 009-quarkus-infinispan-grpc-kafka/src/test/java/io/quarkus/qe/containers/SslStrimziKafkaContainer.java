package io.quarkus.qe.containers;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.testcontainers.containers.Network;
import org.testcontainers.images.builder.Transferable;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ContainerNetwork;

public class SslStrimziKafkaContainer extends KafkaContainerHelper<SslStrimziKafkaContainer> {

    private int kafkaExposedPort;

    public SslStrimziKafkaContainer(final String version) {
        super("quay.io/strimzi/kafka:" + version);
        super.withNetwork(Network.SHARED);

        // exposing kafka port from the container
        withExposedPorts(KAFKA_PORT);

        withEnv("LOG_DIR", "/tmp");
    }

    public SslStrimziKafkaContainer() {
        this("latest-kafka-" + LATEST_KAFKA_VERSION);
    }

    @Override
    protected void containerIsStarting(InspectContainerResponse containerInfo, boolean reused) {
        super.containerIsStarting(containerInfo, reused);

        kafkaExposedPort = getMappedPort(KAFKA_PORT);

        StringBuilder advertisedListeners = new StringBuilder(getBootstrapServers());

        Collection<ContainerNetwork> cns = containerInfo.getNetworkSettings().getNetworks().values();

        for (ContainerNetwork cn : cns) {
            advertisedListeners.append("," + "BROKER://").append(cn.getIpAddress()).append(":9093");
        }

        String command = "#!/bin/bash \n";
        command += "bin/zookeeper-server-start.sh config/zookeeper.properties &\n";
        command += "bin/kafka-server-start.sh config/server.properties --override listeners=BROKER://0.0.0.0:9093,SSL://0.0.0.0:"
                + KAFKA_PORT +
                " --override advertised.listeners=" + advertisedListeners.toString() +
                " --override zookeeper.connect=localhost:" + ZOOKEEPER_PORT +
                " --override listener.security.protocol.map=BROKER:PLAINTEXT,SSL:SSL" +
                " --override inter.broker.listener.name=BROKER\n";

        copyFileToContainer(
                Transferable.of(command.getBytes(StandardCharsets.UTF_8), 700),
                STARTER_SCRIPT);
    }

    public String getBootstrapServers() {
        return String.format("SSL://%s:%s", getContainerIpAddress(), kafkaExposedPort);
    }
}
