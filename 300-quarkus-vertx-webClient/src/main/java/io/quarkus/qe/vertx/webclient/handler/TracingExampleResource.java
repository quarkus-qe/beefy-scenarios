package io.quarkus.qe.vertx.webclient.handler;

import io.quarkus.qe.vertx.webclient.service.PongService;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.smallrye.mutiny.Uni;

import jakarta.inject.Inject;

@RouteBase(path = "/trace")
public class TracingExampleResource {

    @Inject
    PongService pongService;

    @Route(methods = Route.HttpMethod.GET, path = "/ping")
    Uni<String> pingRequest() {
        return pongService.pong().onItem().transform(pong -> "ping-" + pong);
    }
}
