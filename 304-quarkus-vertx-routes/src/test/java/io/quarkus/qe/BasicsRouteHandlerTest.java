package io.quarkus.qe;

import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BasicsRouteHandlerTest {

    @Test
    public void shouldWorkUsingParamsWithUnderscore() {
        given().when()
                .get("/basics/param-with-underscore/work")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Disabled("TODO: Caused by https://github.com/quarkusio/quarkus/issues/15470")
    @Test
    public void shouldWorkCallingAMethodWithEmptyResponse() {
        given().when()
                .get("/basics/method-return-empty/work")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
