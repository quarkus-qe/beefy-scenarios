package io.quarkus.qe.core;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.qe.core.containers.KeycloakTestResource;
import io.quarkus.qe.core.containers.MongoTestResource;
import io.quarkus.qe.core.containers.PostgreSqlDatabaseTestResource;
import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@WithTestResource(value = MongoTestResource.class)
@WithTestResource(value = PostgreSqlDatabaseTestResource.class)
@WithTestResource(value = KeycloakTestResource.class)
public class AlmostAllQuarkusExtensionsTest {

    @Test
    public void testQuarkusEndpointWithManyExtensions() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }

}