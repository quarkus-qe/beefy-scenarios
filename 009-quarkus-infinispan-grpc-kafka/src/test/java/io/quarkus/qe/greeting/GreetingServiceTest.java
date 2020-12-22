package io.quarkus.qe.greeting;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GreetingServiceTest {

    @Test
    public void testGreetingResource() {
        given()
        .when().get("/hello/neo")
        .then().statusCode(200)
                .body(is("Hello neo"));
    }

}