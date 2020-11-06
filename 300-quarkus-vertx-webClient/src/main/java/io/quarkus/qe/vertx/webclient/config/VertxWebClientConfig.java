package io.quarkus.qe.vertx.webclient.config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "vertx.webclient")
public class VertxWebClientConfig {

    @ConfigProperty(name = "timeoutSec", defaultValue = "50")
    public long timeout;

    @ConfigProperty(name = "retries", defaultValue = "1")
    public long retries;
}
