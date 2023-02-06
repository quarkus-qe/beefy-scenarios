package io.quarkus.qe.providers;

import java.io.File;
import java.util.Collections;
import java.util.Properties;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.config.SslConfigs;
import org.eclipse.microprofile.config.inject.ConfigProperty;

public class SslKafkaProvider extends KafkaProviders {

    private final static String SSL_TRUSTSTORE_PASSWORD_CONFIG_VALUE = "top-secret";

    @ConfigProperty(name = "kafka-client-ssl.bootstrap.servers", defaultValue = "localhost:9092")
    String sslKafkaBootStrap;

    @ConfigProperty(name = "ssl-dir", defaultValue = "src/main/resources")
    String sslDir;

    @Produces
    @Named("kafka-consumer-ssl")
    KafkaConsumer<String, String> getSslConsumer() {
        Properties props = setupConsumerProperties(sslKafkaBootStrap, "test-consumer");
        sslSetup(props);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("test-ssl-consumer"));
        return consumer;
    }

    @Produces
    @Named("kafka-producer-ssl")
    KafkaProducer<String, String> getSslProducer() {
        Properties props = setupProducerProperties(sslKafkaBootStrap);
        sslSetup(props);
        return new KafkaProducer<>(props);
    }

    @Produces
    @Named("kafka-admin-ssl")
    AdminClient getSslAdmin() {
        Properties props = setupConsumerProperties(sslKafkaBootStrap, "test-consumer-admin");
        sslSetup(props);
        return KafkaAdminClient.create(props);
    }

    protected void sslSetup(Properties props) {
        File tsFile = new File(sslDir + "/kafka-truststore.p12");
        props.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.setProperty(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, tsFile.getPath());
        props.setProperty(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, SSL_TRUSTSTORE_PASSWORD_CONFIG_VALUE);
        props.setProperty(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "PKCS12");
        props.setProperty(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
    }
}
