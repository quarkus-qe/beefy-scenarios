package io.quarkus.qe.vertx.kafka.config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "vertx.kafka.producer")
public class VertxKProducerConfig {

    @ConfigProperty(name = "delaySec", defaultValue = "1000")
    public long delay;

    @ConfigProperty(name = "batchSize", defaultValue = "100")
    public int batchSize;

}
