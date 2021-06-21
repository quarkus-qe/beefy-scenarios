package io.quarkus.qe.resources;

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
        String sslTruststorePath = new File("src/main/resources").getAbsolutePath() + "/kafka-truststore.p12";
        properties.put("kafka.ssl.truststore.location", sslTruststorePath);
        properties.put("kafka.security.protocol", "SSL");
        properties.put("kafka.ssl.truststore.password", "top-secret");
        properties.put("kafka.ssl.truststore.type", "PKCS12");
        properties.put("quarkus.kafka-streams.ssl.endpoint-identification-algorithm", "");
        properties.put("kafka.bootstrap.servers", kafka.getBootstrapServers());
        properties.put("quarkus.kafka-streams.bootstrap-servers", kafka.getBootstrapServers());
        return properties;
    }

    @Override
    public void stop() {
        if (kafka != null) {
            kafka.close();
        }
    }
}
