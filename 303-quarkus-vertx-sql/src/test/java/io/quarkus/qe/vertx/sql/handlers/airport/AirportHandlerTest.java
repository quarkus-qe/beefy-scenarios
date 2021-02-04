package io.quarkus.qe.vertx.sql.handlers.airport;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public abstract class AirportHandlerTest {
    @Test
    @DisplayName("Retrieve all airports")
    public void retrieveAllAirlines() {
        given().accept(ContentType.JSON)
                .when()
                .get("/airports/")
                .then()
                .statusCode(200)
                .assertThat().body("size()", is(9));
    }
}
