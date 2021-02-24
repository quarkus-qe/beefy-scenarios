package io.quarkus.qe.validation;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;

@RouteBase(path = "/validate")
public class ValidationOnResponseRouteHandler {

    @Route(methods = HttpMethod.GET, path = "/response-uni-valid")
    @Valid
    Uni<Response> validateUniResponseWithTypeReturnsValid() {
        return Uni.createFrom().item(createResponse());
    }

    @Route(methods = HttpMethod.GET, path = "/response-uni-invalid-id")
    @Valid
    Uni<Response> validateUniResponseWithTypeReturnsInvalidID() {
        Response response = createResponse();
        response.setId(null); // it's not invalid!
        return Uni.createFrom().item(response);
    }

    @Route(methods = HttpMethod.GET, path = "/response-uni-invalid-string")
    @Valid
    @Size(min = 3, max = 3, message = "response must have 3 characters")
    Uni<String> validateUniResponseWithStringReturnsInvalidSize() {
        return Uni.createFrom().item("ASDASD");
    }

    @Route(methods = HttpMethod.GET, path = "/response-multi-invalid-id")
    @Valid
    Multi<Response> validateMultiResponseWithTypeReturnsInvalidID() {
        Response response = createResponse();
        response.setId(null); // it's not invalid!
        return Multi.createFrom().items(response);
    }

    private static final Response createResponse() {
        Response response = new Response();
        response.setId("identifier");
        response.setCustom("UPPER");
        return response;
    }
}
