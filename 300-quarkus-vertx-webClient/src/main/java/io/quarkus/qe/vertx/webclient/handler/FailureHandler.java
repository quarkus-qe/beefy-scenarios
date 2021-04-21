package io.quarkus.qe.vertx.webclient.handler;

import java.net.HttpURLConnection;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.vertx.web.Route;
import io.smallrye.mutiny.TimeoutException;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class FailureHandler {
    @Route(path = "/*", type = Route.HandlerType.FAILURE, produces = "application/json")
    void runtimeFailures(RuntimeException e, HttpServerResponse response) {
        if (e instanceof TimeoutException) {
            response.setStatusCode(HttpURLConnection.HTTP_CLIENT_TIMEOUT)
                    .end(Json.encode(new JsonObject().put("msg", e.getMessage())));
        } else {
            response.setStatusCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
                    .end(Json.encode(new JsonObject().put("msg", e.getMessage())));
        }
    }
}
