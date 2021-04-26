package io.quarkus.qe.vertx.sql.handlers.spec;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.restassured.http.ContentType;

public interface AirportHandlerSpec {
    default void retrieveAllAirports() {
        given().accept(ContentType.JSON)
                .when()
                .get("/airports/")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .assertThat().body("size()", is(9));
    }
}
