package io.quarkus.qe.core;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class JavaEELikeQuarkusBundleTest {

    @Test
    public void testJavaEELikeEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(HttpStatus.SC_OK)
             .body(is("hello"));
    }

}