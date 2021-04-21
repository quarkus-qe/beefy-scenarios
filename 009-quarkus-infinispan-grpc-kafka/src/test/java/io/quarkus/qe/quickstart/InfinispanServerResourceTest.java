package io.quarkus.qe.quickstart;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.containers.InfinispanTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(InfinispanTestResource.class)
class InfinispanServerResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/infinispan")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("Hello World, Infinispan is up!"));
    }
}