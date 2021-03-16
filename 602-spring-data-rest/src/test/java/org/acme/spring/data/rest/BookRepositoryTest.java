package org.acme.spring.data.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsNot.not;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.acme.spring.data.rest.containers.PostgreSqlDatabaseTestResource;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;

@QuarkusTest
@QuarkusTestResource(PostgreSqlDatabaseTestResource.class)
class BookRepositoryTest {

    @Test
    void testAllRepositoryMethods() throws InterruptedException {
        //GET - List all books (should have all 4 books the database has initially)
        given()
                .accept("application/json")
                .when().get("/books")
                .then()
                .statusCode(200)
                .body(
                        containsString("Aeneid"),
                        containsString("Beach House"),
                        containsString("Cadillac Desert"),
                        containsString("Dagon and Other Macabre Tales")
                );

        //POST - Create a new Book
        given()
                .contentType("application/json")
                .accept("application/json")
                .body("{\"name\": \"Early Asimov\", \"author\": \"Isaac Asimov\"}")
                .when().post("/books")
                .then()
                .statusCode(201)
                .body(containsString("Early Asimov"))
                .body("id", notNullValue())
                .extract().body().jsonPath().getString("id");

        //PUT - Update a new Book
        given()
                .contentType("application/json")
                .accept("application/json")
                .body("{\"name\": \"Early Asimov 2nd Edition\", \"author\": \"Isaac Asimov\"}")
                .when().put("/books/5")
                .then()
                .statusCode(204);


        //GET{id} - Find new book by id
        given()
                .when().get("/books/id/5")
                .then()
                .statusCode(200)
                .body(
                        containsString("Early Asimov 2nd Edition")
                );

        //DELETE - Try to delete a book via HTTP (method not allowed)
        given()
                .when().delete("/books/5")
                .then()
                .statusCode(405);

        //Test repository pagination
        given()
                .accept("application/json")
                .queryParam("size", "2")
                .queryParam("page", "0")
                .when().get("/books")
                .then()
                .statusCode(200)
                .body(
                        containsString("Aeneid"),
                        containsString("Beach House"),
                        not(containsString("Cadillac Desert")),
                        not(containsString("Dagon and Other Macabre Tales")),
                        not(containsString("Early Asimov 2nd Edition"))
                );

        //Test repository sorting
        List<String> bookNamesSortedDesc = new ArrayList<>(Arrays.asList(
                "Early Asimov 2nd Edition",
                "Dagon and Other Macabre Tales",
                "Cadillac Desert",
                "Beach House",
                "Aeneid"
        ));
        Response response = given()
                .accept("application/json")
                .queryParam("sort", "-name")
                .when().get("/books")
                .then()
                .statusCode(200).extract().response();
        List<String> bookNamesRepositorySortedDesc = response.jsonPath().getList("name");

        Assert.assertEquals(bookNamesSortedDesc, bookNamesRepositorySortedDesc);

    }

    @Test
    void testRepositoryValidator() throws InterruptedException{
        //Try to add a book with invalid constraints
        given()
                .contentType("application/json")
                .body("{\"name\": \"Q\", \"author\": \"Li\"}")
                .when().post("/books")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("length must be between 2 and 50"));
    }
}
