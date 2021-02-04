package io.quarkus.qe.vertx.sql.handlers.PricingRules;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public abstract class PricingRulesHandlerTest {
    @Test
    @DisplayName("Retrieve all pricingRules")
    public void retrieveAllPricingRules() {
        given().accept(ContentType.JSON)
                .when()
                .get("/pricingRules/")
                .then()
                .statusCode(200)
                .assertThat().body("size()", is(4));
    }
}
