package io.quarkus.qe;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;

@QuarkusTest
public class Vertx4IssueTest {

    @Test
    public void shouldWorks() {
        given().when()
                .get("/issue/")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
