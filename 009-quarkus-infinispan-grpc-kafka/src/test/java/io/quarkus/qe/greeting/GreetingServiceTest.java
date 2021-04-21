package io.quarkus.qe.greeting;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GreetingServiceTest {

    @Test
    public void testBlockingGreetingResource() {
        given()
                .when().get("/hello/blocking/neo")
                .then().statusCode(HttpStatus.SC_OK)
                .body(is("Hello neo"));
    }

    @Test
    public void testMutinyGreetingResource() {
        given()
                .when().get("/hello/mutiny/neo")
                .then().statusCode(HttpStatus.SC_OK)
                .body(is("Hello neo"));
    }

}