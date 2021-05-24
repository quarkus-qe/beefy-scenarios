package io.quarkus.qe.vertx.webclient.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "vertx.webclient")
public interface VertxWebClientConfig {

    @WithName("timeout-sec")
    long timeout();

    long retries();
}
