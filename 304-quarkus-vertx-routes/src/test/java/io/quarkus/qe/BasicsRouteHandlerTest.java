package io.quarkus.qe;

import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BasicsRouteHandlerTest {

    @Test
    @Disabled("TODO: It returns HTTP 404 Not Found. Reported in https://github.com/quarkusio/quarkus/issues/15185")
    public void shouldWorkUsingParamsWithHyphen() {
        given().when()
                .get("/param-with-hyphen/work")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
