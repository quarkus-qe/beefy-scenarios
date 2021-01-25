package io.quarkus.qe.vertx.kafka.config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "vertx.kafka.producer")
public class VertxKProducerConfig {

    @ConfigProperty(name = "delaySec")
    public long delay;

    @ConfigProperty(name = "batchSize")
    public int batchSize;

}
