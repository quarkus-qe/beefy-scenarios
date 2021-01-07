package io.quarkus.qe.vertx.web;

import io.quarkus.qe.vertx.resources.RedisResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(RedisResource.class)
public class BladeRunnerHandlerTest extends AbstractCommonTest {
    @Test
    @DisplayName("Retrieve bladeRunner by id")
    public void retrieveBladeRunnerById() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.EMPTY, "admin"))
                .when()
                .get("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(200);
    }
    @Test
    @DisplayName("Retrieve all bladeRunners")
    public void retrieveAllBladeRunners() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.EMPTY, "admin"))
                .when()
                .get("/bladeRunner/")
                .then()
                .assertThat().body("size()", is(1))
                .statusCode(200);
    }
    @Test
    @DisplayName("Delete bladeRunner")
    public void deleteBladeRunner() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.EMPTY, "admin"))
                .when()
                .delete("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(204);
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.EMPTY, "admin"))
                .when()
                .get("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(404);
    }
}