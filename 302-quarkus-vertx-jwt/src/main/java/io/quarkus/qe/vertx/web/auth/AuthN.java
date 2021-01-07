package io.quarkus.qe.vertx.web.auth;

import io.quarkus.qe.vertx.web.config.AuthNConfig;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.mutiny.core.Vertx;
import java.util.Arrays;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class AuthN {

    @Inject
    AuthNConfig authNConf;

    @Inject
    Vertx vertx;

    @Produces
    JWTAuth jwtAuth() {
        return JWTAuth.create(vertx.getDelegate(), new JWTAuthOptions().setJWTOptions(getJwtOptions())
                .addPubSecKey(new PubSecKeyOptions(authConfig())));
    }

    private JsonObject authConfig(){
        return new JsonObject()
                .put("symmetric", true)
                .put("algorithm", authNConf.alg)
                .put("publicKey", authNConf.secret);
    }

    private JWTOptions getJwtOptions() {
        return new JWTOptions()
                .setIgnoreExpiration(false)
                .setIssuer(authNConf.jwt.claims.iss)
                .setAudience(Arrays.asList((authNConf.jwt.claims.aud)))
                .setSubject(authNConf.jwt.claims.sub)
                .setExpiresInMinutes(authNConf.jwt.livespam)
                .setAlgorithm(authNConf.alg);
    }

}
