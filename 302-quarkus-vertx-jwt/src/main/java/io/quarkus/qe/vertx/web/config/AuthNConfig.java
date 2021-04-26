package io.quarkus.qe.vertx.web.config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "authN")
public class AuthNConfig {
    public String alg;

    public String secret;

    @ConfigProperty(name = "tokenLiveSpanMin")
    public int liveSpan;

    @ConfigProperty(name = "jwt.claims")
    public JwtClaims claims;
}
