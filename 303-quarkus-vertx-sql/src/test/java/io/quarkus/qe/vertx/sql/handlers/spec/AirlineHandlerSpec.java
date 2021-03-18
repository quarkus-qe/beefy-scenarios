package io.quarkus.qe.vertx.sql.handlers.spec;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.restassured.http.ContentType;

public interface AirlineHandlerSpec {
    default void retrieveAllAirlines() {
        given().accept(ContentType.JSON)
                .when()
                .get("/airlines/")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .assertThat().body("size()", is(7));
    }
}
