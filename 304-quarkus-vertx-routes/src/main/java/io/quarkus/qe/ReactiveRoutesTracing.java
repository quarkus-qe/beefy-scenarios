package io.quarkus.qe;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.ext.web.RoutingContext;

@RouteBase(path = "/trace")
public class ReactiveRoutesTracing {
    @Route(methods = Route.HttpMethod.GET, path = "/hello")
    boolean validateRequestSinglePara() {
        return true;
    }
}
