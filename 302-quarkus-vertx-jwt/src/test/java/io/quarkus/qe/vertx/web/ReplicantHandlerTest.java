package io.quarkus.qe.vertx.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.vertx.resources.RedisResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@QuarkusTestResource(RedisResource.class)
public class ReplicantHandlerTest extends AbstractCommonTest {
    @Test
    @DisplayName("Retrieve replicant by id")
    public void retrieveReplicantById() {
        given().accept(ContentType.JSON)
                .when()
                .get("/replicant/" + replicant.getId())
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Retrieve all replicants")
    public void retrieveAllReplicant() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.EMPTY, "admin"))
                .when()
                .get("/replicant/")
                .then()
                .assertThat().body("size()", is(1))
                .statusCode(200);
    }

    @Test
    @DisplayName("Delete replicant")
    public void deleteReplicant() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.EMPTY, "admin"))
                .when()
                .delete("/replicant/" + replicant.getId())
                .then()
                .statusCode(204);
        given().accept(ContentType.JSON)
                .when()
                .get("/replicant/" + replicant.getId())
                .then()
                .statusCode(404);
    }
}
