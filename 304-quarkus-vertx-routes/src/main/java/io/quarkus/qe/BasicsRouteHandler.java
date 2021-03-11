package io.quarkus.qe;

import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.core.http.HttpMethod;

@RouteBase(path = "/basics")
public class BasicsRouteHandler {
    @Route(methods = HttpMethod.GET, path = "/param-with-underscore/:first_param")
    boolean validateRequestSingleParam(@Param("first_param") String param) {
        return true;
    }
}
