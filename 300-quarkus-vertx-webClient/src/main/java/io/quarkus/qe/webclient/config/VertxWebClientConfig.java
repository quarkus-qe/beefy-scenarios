package io.quarkus.qe.webclient.config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "vertx.webclient")
public class VertxWebClientConfig {

    @ConfigProperty(name = "timeoutSec")
    public long timeout;

    @ConfigProperty(name = "retries")
    public long retries;
}
