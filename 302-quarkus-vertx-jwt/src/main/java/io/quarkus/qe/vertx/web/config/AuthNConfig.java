package io.quarkus.qe.vertx.web.config;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "authN")
public class AuthNConfig {
    public String alg;

    public String secret;

    public Jwt jwt;
}
