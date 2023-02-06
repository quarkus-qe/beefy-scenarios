package io.quarkus.qe.providers;

import java.util.Collections;
import java.util.Properties;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.config.SaslConfigs;
import org.eclipse.microprofile.config.inject.ConfigProperty;

public class SaslKafkaProvider extends KafkaProviders {

    private final static String SASL_USERNAME_VALUE = "client";
    private final static String SASL_PASSWORD_VALUE = "client-secret";

    @ConfigProperty(name = "kafka-client-sasl.bootstrap.servers", defaultValue = "localhost:9092")
    String saslKafkaBootStrap;

    @Produces
    @Named("kafka-consumer-sasl")
    KafkaConsumer<String, String> getSaslConsumer() {
        Properties props = setupConsumerProperties(saslKafkaBootStrap, "test-consumer");
        saslSetup(props);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("test-ssl-consumer"));
        return consumer;
    }

    @Produces
    @Named("kafka-producer-sasl")
    KafkaProducer<String, String> getSaslProducer() {
        Properties props = setupProducerProperties(saslKafkaBootStrap);
        saslSetup(props);
        return new KafkaProducer<>(props);
    }

    @Produces
    @Named("kafka-admin-sasl")
    AdminClient getSaslAdmin() {
        Properties props = setupConsumerProperties(saslKafkaBootStrap, "test-consumer-admin");
        saslSetup(props);
        return KafkaAdminClient.create(props);
    }

    private static void saslSetup(Properties props) {
        props.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        props.setProperty(SaslConfigs.SASL_MECHANISM, "PLAIN");
        props.setProperty(SaslConfigs.SASL_JAAS_CONFIG,
                "org.apache.kafka.common.security.plain.PlainLoginModule required "
                        + "username=\"" + SASL_USERNAME_VALUE + "\" "
                        + "password=\"" + SASL_PASSWORD_VALUE + "\";");
    }
}
