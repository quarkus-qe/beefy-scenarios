package io.quarkus.qe.kafka.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "vertx.kafka.producer")
public class VertxKProducerConfig {

    @ConfigProperty(name = "delayMs")
    public long delay;

    @ConfigProperty(name = "batchSize")
    public int batchSize;

}
