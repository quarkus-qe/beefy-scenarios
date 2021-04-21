package io.quarkus.qe.vertx.sql.handlers.spec;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.restassured.http.ContentType;

public interface PricingRulesSpec {

    int EXPECTED_PRICING_RULES_COUNT = 4;

    default void retrieveAllPricingRules() {
        given().accept(ContentType.JSON)
                .when()
                .get("/pricingRules/")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .assertThat().body("size()", is(EXPECTED_PRICING_RULES_COUNT));
    }
}
