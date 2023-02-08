package io.quarkus.qe.vertx.web.auth;

import java.util.Arrays;

import io.quarkus.qe.vertx.web.config.AuthNConfig;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.mutiny.core.Vertx;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

public class AuthN {

    @Inject
    AuthNConfig authNConf;

    @Inject
    Vertx vertx;

    @Produces
    JWTAuth jwtAuth() {
        return JWTAuth.create(vertx.getDelegate(), new JWTAuthOptions().setJWTOptions(getJwtOptions())
                .addPubSecKey(getPubSecKeyOptions()));
    }

    private PubSecKeyOptions getPubSecKeyOptions() {
        JsonObject authConfig = new JsonObject()
                .put("symmetric", true)
                .put("algorithm", authNConf.alg())
                .put("publicKey", authNConf.secret());

        return new PubSecKeyOptions(authConfig).setBuffer(authConfig.getBuffer("publicKey"));
    }

    private JWTOptions getJwtOptions() {
        return new JWTOptions()
                .setIgnoreExpiration(false)
                .setIssuer(authNConf.claims().iss())
                .setAudience(Arrays.asList((authNConf.claims().aud())))
                .setSubject(authNConf.claims().sub())
                .setExpiresInMinutes(authNConf.liveSpan())
                .setAlgorithm(authNConf.alg());
    }

}
