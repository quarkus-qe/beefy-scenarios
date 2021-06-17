package io.quarkus.qe.containers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.logging.Logger;
import org.testcontainers.containers.GenericContainer;

import io.reactivex.rxjava3.annotations.NonNull;

public class KafkaContainerHelper<SELF extends GenericContainer<SELF>> extends GenericContainer<SELF> {

    private static final Logger LOGGER = Logger.getLogger(KafkaContainerHelper.class.getName());
    private static final List<String> supportedKafkaVersions = new ArrayList<>(3);
    protected static final String LATEST_KAFKA_VERSION;
    protected static final String STARTER_SCRIPT = "/testcontainers_start.sh";
    protected static final int KAFKA_PORT = 9092;
    protected static final int ZOOKEEPER_PORT = 2181;

    static {
        InputStream inputStream = io.strimzi.StrimziKafkaContainer.class.getResourceAsStream("/kafka-versions.txt");
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        try (BufferedReader bufferedReader = new BufferedReader(streamReader)) {
            String kafkaVersion;
            while ((kafkaVersion = bufferedReader.readLine()) != null) {
                supportedKafkaVersions.add(kafkaVersion);
            }
        } catch (IOException e) {
            LOGGER.error("Unable to load the supported Kafka versions", e);
        }

        // sort kafka version from low to high
        Collections.sort(supportedKafkaVersions);

        LATEST_KAFKA_VERSION = supportedKafkaVersions.get(supportedKafkaVersions.size() - 1);
    }

    public KafkaContainerHelper(@NonNull String dockerImageName) {
        super(dockerImageName);
    }

    public void doStart() {
        withCommand("sh", "-c", "while [ ! -f " + STARTER_SCRIPT + " ]; do sleep 0.1; done; " + STARTER_SCRIPT);
        super.doStart();
    }

}
