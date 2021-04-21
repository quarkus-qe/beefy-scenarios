package io.quarkus.qe.vertx.web;

import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.vertx.resources.RedisResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@QuarkusTestResource(RedisResource.class)
public class SecuredResourceTest extends AbstractCommonTest {

    @Test
    @DisplayName("secured resource. Valid Token")
    public void validJwtToken() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + generateToken(Invalidity.EMPTY, "admin"))
                .when()
                .get("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("secured resource. Expired Token")
    public void expiredJwtToken() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + generateToken(Invalidity.EXPIRED, "admin"))
                .when()
                .get("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("secured resource. Ahead of time")
    public void aotJwtToken() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + generateToken(Invalidity.AHEAD_OF_TIME, "admin"))
                .when()
                .get("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("secured resource. Invalid issuer")
    public void invalidIssuer() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + generateToken(Invalidity.WRONG_ISSUER, "admin"))
                .when()
                .get("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("secured resource. Invalid Audience")
    public void invalidAudience() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + generateToken(Invalidity.WRONG_AUDIENCE, "admin"))
                .when()
                .get("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("secured resource. Wrong Key")
    public void wrongKey() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + generateToken(Invalidity.WRONG_KEY, "admin"))
                .when()
                .get("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

}
