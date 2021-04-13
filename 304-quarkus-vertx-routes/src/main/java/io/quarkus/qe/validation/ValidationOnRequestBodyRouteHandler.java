package io.quarkus.qe.validation;

import javax.validation.Valid;

import io.quarkus.vertx.web.Body;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.Route.HttpMethod;
import io.quarkus.vertx.web.RouteBase;

@RouteBase(path = "/validate")
public class ValidationOnRequestBodyRouteHandler {
    @Route(methods = HttpMethod.POST, path = "/request-body")
    boolean validateRequestBody(@Body @Valid Request param) {
        return true;
    }
}
