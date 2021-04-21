package io.quarkus.qe.core;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.core.containers.MongoTestResource;
import io.quarkus.qe.core.containers.PostgreSqlDatabaseTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(value = MongoTestResource.class)
@QuarkusTestResource(value = PostgreSqlDatabaseTestResource.class)
public class AlmostAllQuarkusExtensionsTest {

    @Test
    public void testQuarkusEndpointWithManyExtensions() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("hello"));
    }

}