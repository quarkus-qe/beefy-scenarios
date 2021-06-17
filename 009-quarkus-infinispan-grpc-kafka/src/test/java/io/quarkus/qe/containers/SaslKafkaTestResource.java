package io.quarkus.qe.containers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testcontainers.utility.MountableFile;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class SaslKafkaTestResource implements QuarkusTestResourceLifecycleManager {

    private final SaslStrimziKafkaContainer kafka = new SaslStrimziKafkaContainer()
            .withCopyFileToContainer(MountableFile.forClasspathResource("server-sasl.properties"),
                    "/opt/kafka/config/server.properties");

    @Override
    public Map<String, String> start() {
        kafka.start();
        Map<String, String> properties = new HashMap<>();
        properties.put("ssl-dir", new File("src/test/resources").getAbsolutePath());
        properties.put("kafka-client-sasl.bootstrap.servers", kafka.getBootstrapServers());
        return properties;
    }

    @Override
    public void stop() {
        if (kafka != null) {
            kafka.close();
        }
    }
}
