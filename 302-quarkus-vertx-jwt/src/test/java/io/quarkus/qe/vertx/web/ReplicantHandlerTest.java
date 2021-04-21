package io.quarkus.qe.vertx.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.apache.http.HttpStatus;
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
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Retrieve all replicants")
    public void retrieveAllReplicant() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + generateToken(Invalidity.EMPTY, "admin"))
                .when()
                .get("/replicant/")
                .then()
                .assertThat().body("size()", is(1))
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Delete replicant")
    public void deleteReplicant() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + generateToken(Invalidity.EMPTY, "admin"))
                .when()
                .delete("/replicant/" + replicant.getId())
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
        given().accept(ContentType.JSON)
                .when()
                .get("/replicant/" + replicant.getId())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
