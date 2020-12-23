package io.quarkus.qe.vertx.web;

import io.quarkus.qe.vertx.resources.RedisResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

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
