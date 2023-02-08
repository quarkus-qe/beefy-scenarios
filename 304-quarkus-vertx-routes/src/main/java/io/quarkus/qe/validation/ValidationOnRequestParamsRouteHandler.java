package io.quarkus.qe.validation;

import io.quarkus.qe.validation.annotations.Uppercase;
import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.Route.HttpMethod;
import io.quarkus.vertx.web.RouteBase;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@RouteBase(path = "/validate")
public class ValidationOnRequestParamsRouteHandler {
    @Route(methods = HttpMethod.GET, path = "/request-single-param/:firstParam")
    boolean validateRequestSingleParam(
            @Param("firstParam") @Size(min = 3, max = 3, message = "Param must have 3 characters") String param) {
        return true;
    }

    @Route(methods = HttpMethod.GET, path = "/request-multiple-param/:firstParam/second/:secondParam")
    boolean validateRequestMultipleParam(
            @Param("firstParam") @Size(min = 3, max = 3, message = "First param must have 3 characters") String firstParam,
            @Param("secondParam") @Pattern(regexp = "[A-Z]{2}[0-9]{3}", message = "Second param must match pattern") String secondParam) {
        return true;
    }

    @Route(methods = HttpMethod.GET, path = "/request-single-param-custom-validation/:firstParam")
    boolean validateRequestSingleParamUsingCustomValidation(
            @Param("firstParam") @Uppercase String param) {
        return true;
    }
}
