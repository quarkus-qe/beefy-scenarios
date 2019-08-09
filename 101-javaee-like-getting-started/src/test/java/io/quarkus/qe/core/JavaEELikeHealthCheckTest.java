package io.quarkus.qe.core;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class JavaEELikeHealthCheckTest {

    @Test
    public void testHealthEndpoint() {
        given()
                .when().get("/health")
                .then()
                .statusCode(200)
                .body("status", is("UP"),
                        "checks.name", contains("greeting"),
                        "checks.status", contains("UP")
                );
    }

}