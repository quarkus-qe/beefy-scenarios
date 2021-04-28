package io.quarkus.qe.rest.data;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsNot.not;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.containers.MySqlDatabaseTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(MySqlDatabaseTestResource.class)
class UserResourceTest {

    private final static String NEW_USER_ID = "3";

    @Test
    void testAllRepositoryMethods() {

        //GET - List all users
        //Assert that order matches. UserRepository override it to be ascend. Initial order defined by import.sql
        String userList = given()
                .accept("application/hal+json")
                .when().get("/users/all")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response().jsonPath().getString("_embedded.user_list.name");
        Assert.assertEquals("[Alaba, Balaba]", userList);

        //POST - Create a new User
        given()
                .contentType("application/json")
                .accept("application/json")
                .body("{\"name\": \"Culibaba\"}")
                .when().post("/users")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body(containsString("Culibaba"))
                .body("id", notNullValue());

        //PUT - Update a new User (method not allowed)
        given()
                .contentType("application/json")
                .accept("application/json")
                .body("{\"name\": \"Donbaba\"}")
                .when().put("/users/" + NEW_USER_ID)
                .then()
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);

        //GET{id} - Find new user by id
        given()
                .when().get("/users/" + NEW_USER_ID)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(
                        containsString("Culibaba"));

        //DELETE - Delete new user via HTTP
        given()
                .when().delete("/users/" + NEW_USER_ID)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        //Test repository pagination
        given()
                .accept("application/json")
                .queryParam("size", "1")
                .queryParam("page", "0")
                .when().get("/users/all")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(
                        containsString("Alaba"),
                        not(containsString("Balaba")));
    }
}
