package org.acme.quickstart;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@QuarkusTestResource(CacheResource.class)
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