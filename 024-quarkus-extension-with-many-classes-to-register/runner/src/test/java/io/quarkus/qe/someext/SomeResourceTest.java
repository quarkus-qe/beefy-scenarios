package io.quarkus.qe.someext;

import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@QuarkusTest
class SomeResourceTest {

    @Test
    void testEcho() {
        RestAssured.given() //
                .contentType(ContentType.TEXT)
                .body("Hello World")
                .post("/echo") //
                .then()
                .body(equalTo("Hello World"))
                .statusCode(200);
    }
}
