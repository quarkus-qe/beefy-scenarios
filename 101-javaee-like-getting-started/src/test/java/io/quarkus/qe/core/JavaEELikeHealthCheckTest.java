package io.quarkus.qe.core;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class JavaEELikeHealthCheckTest {

    @Test
    public void testHealthEndpoint() {
        given()
                .when().get("/health")
                .then()
                .statusCode(200)
                .body("status", is("UP"),
                        "checks.name", containsInAnyOrder("liveness", "readiness", "greeting", "Database connections health check"),
                        "checks.status", hasSize(4),
                        "checks.status", hasItem("UP"),
                        "checks.status", not(hasItem("DOWN"))
                );
    }
    @Test
    public void testReadinessEndpoint() {
        given()
                .when().get("/health/ready")
                .then()
                .statusCode(200)
                .body("status", is("UP"),
                        "checks.name", containsInAnyOrder("readiness", "Database connections health check"),
                        "checks.status", hasSize(2),
                        "checks.status", hasItem("UP")
                );
    }
    @Test
    public void testLivenessEndpoint() {
        given()
                .when().get("/health/live")
                .then()
                .statusCode(200)
                .body("status", is("UP"),
                        "checks.name", hasItems("liveness"),
                        "checks.status", hasSize(1),
                        "checks.status", hasItem("UP")
                );
    }

}