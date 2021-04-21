package io.quarkus.qe.vertx.web.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "authN")
public class AuthNConfig {
    public String alg;

    public String secret;

    @ConfigProperty(name = "tokenLiveSpanMin")
    public int liveSpan;

    @ConfigProperty(name = "jwt.claims")
    public JwtClaims claims;
}
