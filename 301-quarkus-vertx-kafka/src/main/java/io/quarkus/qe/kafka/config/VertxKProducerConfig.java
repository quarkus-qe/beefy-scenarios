package io.quarkus.qe.kafka.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "vertx.kafka.producer")
public interface VertxKProducerConfig {

    @WithName("delay-ms")
    long delay();

    int batchSize();
}
