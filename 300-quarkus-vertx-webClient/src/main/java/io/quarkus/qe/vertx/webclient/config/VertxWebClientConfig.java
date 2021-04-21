package io.quarkus.qe.vertx.webclient.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "vertx.webclient")
public class VertxWebClientConfig {

    @ConfigProperty(name = "timeoutSec")
    public long timeout;

    @ConfigProperty(name = "retries")
    public long retries;
}
