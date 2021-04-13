package io.quarkus.qe.vertx.web.handlers;

import java.time.ZonedDateTime;
import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;

@ApplicationScoped
public class JWTHandler {

    @Inject
    JWTAuth jwt;

    private static final int TOKEN_EXP_MIN = 10;

    public void createJwt(final RoutingContext context){
        String name = context.request().getParam("name");
        context.response()
                .putHeader("Content-Type", "application/json")
                .end(new JsonObject().put("jwt",  jwt.generateToken(defaultClaims(name, "admin"))).encode());
    }

    private JsonObject defaultClaims(String name, String... groups){
        Long now = currentTimeEpoch();
        Long expiration = currentTimePLusOneEpoch();
        return new JsonObject()
                .put("name", name)
                .put("sub", "bff")
                .put("iss", "vertxJWT@redhat.com")
                .put("aud", "third_party")
                .put("groups", Arrays.asList(groups))
                .put("iat", now)
                .put("exp", expiration);
    }

    private Long currentTimeEpoch() {
        return currentTime().toInstant().toEpochMilli() / 1000L;
    }

    private Long currentTimePLusOneEpoch(){
        return currentTime().plusMinutes(TOKEN_EXP_MIN).toInstant().toEpochMilli() / 1000L;
    }

    private ZonedDateTime currentTime() {
        return ZonedDateTime.now();
    }
}
