package io.quarkus.qe.core;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

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
    public void testHealthGroupEndpoint() {
        // TODO: There is an inconsistency about the Health groups path. Reported by https://github.com/quarkusio/quarkus/issues/16389.
        given()
                .when().get("/health/group/customGroup")
                .then()
                .statusCode(200)
                .body("status", is("UP"),
                        "checks.name",
                        containsInAnyOrder("custom-group"),
                        "checks.status", hasSize(1),
                        "checks.status", hasItem("UP"));
    }

    @Test
    public void testReadinessEndpoint() {
        given()
                .when().get("/health/ready")
                .then()
                .statusCode(200)
                .body("status", is("UP"),
                        "checks.name", containsInAnyOrder("readiness", "greeting", "Database connections health check"),
                        "checks.status", hasSize(3),
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