package io.quarkus.qe.vertx.web;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.vertx.resources.RedisResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(RedisResource.class)
public class NoSecuredResourceTest {
    @Test
    @DisplayName("no-secured resource. ")
    public void httpServer() {
        given().when().get("/replicant/noExistID")
                .then()
                .statusCode(404);
    }
}
