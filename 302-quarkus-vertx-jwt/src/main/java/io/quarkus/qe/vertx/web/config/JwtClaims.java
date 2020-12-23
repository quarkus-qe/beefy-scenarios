package io.quarkus.qe.vertx.web.config;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "authN.jwt.claims")
public class JwtClaims {
    public String iss;
    public String sub;
    public String aud;
}
