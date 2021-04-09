package io.quarkus.qe;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.ext.web.RoutingContext;

@RouteBase(path = "issue", produces = "application/json")
public class Vertx4IssueReproducer {

    @Route(methods = Route.HttpMethod.GET, path = "/*")
    void issueExample(RoutingContext context) {
         context.response().end("Hello World!");
    }

}
