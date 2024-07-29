package io.quarkus.qe.vertx.web;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.vertx.resources.RedisResource;
import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@WithTestResource(RedisResource.class)
public class SecuredResourceTest extends AbstractCommonTest {

    @Test
    @DisplayName("secured resource. Valid Token")
    public void validJwtToken() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.EMPTY, "admin"))
                .when()
                .get("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("secured resource. Expired Token")
    public void expiredJwtToken() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.EXPIRED, "admin"))
                .when()
                .get("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("secured resource. Ahead of time")
    public void aotJwtToken() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.AHEAD_OF_TIME, "admin"))
                .when()
                .get("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("secured resource. Invalid issuer")
    public void invalidIssuer() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.WRONG_ISSUER, "admin"))
                .when()
                .get("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("secured resource. Invalid Audience")
    public void invalidAudience() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.WRONG_AUDIENCE, "admin"))
                .when()
                .get("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("secured resource. Wrong Key")
    public void wrongKey() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.WRONG_KEY, "admin"))
                .when()
                .get("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(401);
    }

}
