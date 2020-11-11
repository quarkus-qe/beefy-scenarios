package org.acme.quickstart;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.acme.quickstart.containers.InfinispanTestResource;
import org.junit.jupiter.api.Test;

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
                .statusCode(200)
                .body(is("Hello World, Infinispan is up!"));
    }
}