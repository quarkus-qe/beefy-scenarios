package io.quarkus.qe.validation;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.Route.HttpMethod;
import io.quarkus.vertx.web.RouteBase;
import io.smallrye.mutiny.Uni;

@RouteBase(path = "/validate")
public class ValidationOnResponseRouteHandler {

    private static final int SIZE = 3;

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
    @Size(min = SIZE, max = SIZE, message = "response must have 3 characters")
    Uni<String> validateUniResponseWithStringReturnsInvalidSize() {
        return Uni.createFrom().item("ASDASD");
    }

    private static Response createResponse() {
        Response response = new Response();
        response.setId("identifier");
        response.setCustom("UPPER");
        return response;
    }
}
