package io.quarkus.qe.vertx.sql.handlers.Airline;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public abstract class AirlineHandlerTest {
    @Test
    @DisplayName("Retrieve all airlines")
    public void retrieveAllAirlines() {
        given().accept(ContentType.JSON)
                .when()
                .get("/airlines/")
                .then()
                .statusCode(200)
                .assertThat().body("size()", is(7));
    }
}
