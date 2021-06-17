package io.quarkus.qe.containers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testcontainers.utility.MountableFile;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class SslStrimziKafkaTestResource implements QuarkusTestResourceLifecycleManager {

    private final SslStrimziKafkaContainer kafka = new SslStrimziKafkaContainer()
            .withCopyFileToContainer(MountableFile.forClasspathResource("server-ssl.properties"),
                    "/opt/kafka/config/server.properties")
            .withCopyFileToContainer(MountableFile.forClasspathResource("kafka-keystore.p12"),
                    "/opt/kafka/config/kafka-keystore.p12")
            .withCopyFileToContainer(MountableFile.forClasspathResource("kafka-truststore.p12"),
                    "/opt/kafka/config/kafka-truststore.p12");

    @Override
    public Map<String, String> start() {
        kafka.start();
        Map<String, String> properties = new HashMap<>();
        properties.put("ssl-dir", new File("src/test/resources").getAbsolutePath());
        properties.put("kafka-client-ssl.bootstrap.servers", kafka.getBootstrapServers());
        return properties;
    }

    @Override
    public void stop() {
        if (kafka != null) {
            kafka.close();
        }
    }
}