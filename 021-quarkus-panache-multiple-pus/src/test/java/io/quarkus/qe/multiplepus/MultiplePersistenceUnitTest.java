package io.quarkus.qe.multiplepus;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.multiplepus.containers.MariaDbDatabaseTestResource;
import io.quarkus.qe.multiplepus.containers.PostgreSqlDatabaseTestResource;
import io.quarkus.qe.multiplepus.model.fruit.Fruit;
import io.quarkus.qe.multiplepus.model.vegetable.Vegetable;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@QuarkusTestResource(MariaDbDatabaseTestResource.class)
@QuarkusTestResource(PostgreSqlDatabaseTestResource.class)
class MultiplePersistenceUnitTest {

    private static final int EXPECTED_FRUITS_COUNT = 7;
    private static final int EXPECTED_VEGETABLES_COUNT = 7;
    private static final long INVALID_ID = 999L;

    private static boolean shouldCleanupFruit = false;
    private static boolean shouldCleanupVegetable = false;

    private static int latestFruitId = EXPECTED_FRUITS_COUNT;
    private static int latestVegetableId = EXPECTED_VEGETABLES_COUNT;

    @BeforeEach
    public void cleanUp() {
        if (shouldCleanupFruit) {
            when()
                    .delete("/fruit/" + latestFruitId)
                    .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
            shouldCleanupFruit = false;
        }
        if (shouldCleanupVegetable) {
            when()
                    .delete("/vegetable/" + latestVegetableId)
                    .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
            shouldCleanupVegetable = false;
        }
    }

    @Test
    public void getAllFruits() {
        when()
                .get("/fruit")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("", hasSize(EXPECTED_FRUITS_COUNT));
    }

    @Test
    public void getFruitById() {
        when()
                .get("/fruit/7")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo("Cranberry"));
    }

    @Test
    public void createFruit() {
        Fruit fruit = new Fruit();
        fruit.name = "Canteloupe";

        latestFruitId++;

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(fruit)
                .post("/fruit")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("id", equalTo(latestFruitId))
                .body("name", equalTo("Canteloupe"));
        shouldCleanupFruit = true;

        when()
                .get("/fruit/" + latestFruitId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo("Canteloupe"));
    }

    @Test
    public void createInvalidPayloadFruit() {
        given()
                .when()
                .contentType(ContentType.TEXT)
                .body("")
                .post("/fruit")
                .then()
                .statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE)
                .body("code", equalTo(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE));
    }

    @Test
    public void createInvalidIdFruit() {
        Fruit fruit = new Fruit();
        fruit.id = INVALID_ID;
        fruit.name = "foo";

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(fruit)
                .post("/fruit")
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("code", equalTo(HttpStatus.SC_UNPROCESSABLE_ENTITY))
                .body("error", equalTo("unexpected ID in request"));
    }

    @Test
    public void updateFruit() {
        Fruit fruit = new Fruit();
        fruit.name = "Canteloupe";

        latestFruitId++;

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(fruit)
                .post("/fruit")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("id", equalTo(latestFruitId))
                .body("name", equalTo("Canteloupe"));

        Fruit updatedFruit = new Fruit();
        updatedFruit.id = (long) latestFruitId;
        updatedFruit.name = "Dragonfruit";

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(updatedFruit)
                .put("/fruit/" + latestFruitId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(latestFruitId))
                .body("name", equalTo("Dragonfruit"));
        shouldCleanupFruit = true;
        when()
                .get("/fruit/" + latestFruitId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo("Dragonfruit"));
    }

    @Test
    public void updateFruitWithUnknownId() {
        Fruit fruit = new Fruit();
        fruit.id = INVALID_ID;
        fruit.name = "foo";

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(fruit)
                .put("/fruit/" + INVALID_ID)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("code", equalTo(HttpStatus.SC_NOT_FOUND))
                .body("error", equalTo("fruit '" + INVALID_ID + "' not found"));
    }

    @Test
    public void updateWithNullFruit() {
        given()
                .when()
                .contentType(ContentType.TEXT)
                .body("")
                .put("/fruit/" + latestFruitId)
                .then()
                .statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE)
                .body("code", equalTo(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE));
    }

    @Test
    public void updateWithInvalidFruit() {
        Fruit fruit = new Fruit();

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(fruit)
                .put("/fruit/" + latestFruitId)
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("code", equalTo(HttpStatus.SC_UNPROCESSABLE_ENTITY))
                .body("error.message", contains("Fruit name must be set!"));
    }

    @Test
    public void deleteFruit() {
        Fruit fruit = new Fruit();
        fruit.name = "Canteloupe";

        latestFruitId++;

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(fruit)
                .post("/fruit")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("id", equalTo(latestFruitId))
                .body("name", equalTo("Canteloupe"));

        when()
                .delete("/fruit/" + latestFruitId)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        when()
                .get("/fruit/" + latestFruitId)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("code", equalTo(HttpStatus.SC_NOT_FOUND))
                .body("error", equalTo("fruit '" + latestFruitId + "' not found"));
    }

    @Test
    public void deleteFruitWithUnknownId() {
        when()
                .delete("/fruit/" + INVALID_ID)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("code", equalTo(HttpStatus.SC_NOT_FOUND))
                .body("error", equalTo("fruit '" + INVALID_ID + "' not found"));
    }

    @Test
    public void getAllVegetables() {
        when()
                .get("/vegetable")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("", hasSize(EXPECTED_VEGETABLES_COUNT));
    }

    @Test
    public void getVegetableById() {
        when()
                .get("/vegetable/" + latestVegetableId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo("Garlic"));
    }

    @Test
    public void createVegetable() {
        Vegetable vegetable = new Vegetable();
        vegetable.name = "Eggplant";

        latestVegetableId++;

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(vegetable)
                .post("/vegetable")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("id", equalTo(latestVegetableId))
                .body("name", equalTo("Eggplant"));
        shouldCleanupVegetable = true;
        when()
                .get("/vegetable/" + latestVegetableId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo("Eggplant"));
    }

    @Test
    public void createNullVegetable() {
        given()
                .when()
                .contentType(ContentType.TEXT)
                .body("")
                .post("/vegetable")
                .then()
                .statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE)
                .body("code", equalTo(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE));
    }

    @Test
    public void createInvalidIdVegetable() {
        Vegetable vegetable = new Vegetable();
        vegetable.id = INVALID_ID;
        vegetable.name = "foo";

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(vegetable)
                .post("/vegetable")
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("code", equalTo(HttpStatus.SC_UNPROCESSABLE_ENTITY))
                .body("error", equalTo("unexpected ID in request"));
    }

    @Test
    public void updateVegetable() {
        Vegetable vegetable = new Vegetable();
        vegetable.name = "Eggplant";

        latestVegetableId++;

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(vegetable)
                .post("/vegetable")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("id", equalTo(latestVegetableId))
                .body("name", equalTo("Eggplant"));

        Vegetable updatedVegetable = new Vegetable();
        updatedVegetable.id = (long) latestVegetableId;
        updatedVegetable.name = "Okra";

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(updatedVegetable)
                .put("/vegetable/" + latestVegetableId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(latestVegetableId))
                .body("name", equalTo("Okra"));

        shouldCleanupVegetable = true;
        when()
                .get("/vegetable/" + latestVegetableId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo("Okra"));
    }

    @Test
    public void updateVegetableWithUnknownId() {
        Vegetable vegetable = new Vegetable();
        vegetable.id = INVALID_ID;
        vegetable.name = "foo";

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(vegetable)
                .put("/vegetable/999")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("code", equalTo(HttpStatus.SC_NOT_FOUND))
                .body("error", equalTo("vegetable '999' not found"));
    }

    @Test
    public void updateWithNullVegetable() {
        given()
                .when()
                .contentType(ContentType.TEXT)
                .body("")
                .put("/vegetable/" + latestVegetableId)
                .then()
                .statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE)
                .body("code", equalTo(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE));
    }

    @Test
    public void updateWithInvalidVegetable() {
        Vegetable vegetable = new Vegetable();

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(vegetable)
                .put("/vegetable/" + latestVegetableId)
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("code", equalTo(HttpStatus.SC_UNPROCESSABLE_ENTITY))
                .body("error.message", contains("Vegetable name must be set!"));
    }

    @Test
    public void deleteVegetable() {
        Vegetable vegetable = new Vegetable();
        vegetable.name = "Eggplant";

        latestVegetableId++;

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(vegetable)
                .post("/vegetable")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("id", equalTo(latestVegetableId))
                .body("name", equalTo("Eggplant"));

        when()
                .delete("/vegetable/" + latestVegetableId)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        when()
                .get("/vegetable/" + latestVegetableId)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("code", equalTo(HttpStatus.SC_NOT_FOUND))
                .body("error", equalTo("vegetable '" + latestVegetableId + "' not found"));
    }

    @Test
    public void deleteVegetableWithUnknownId() {
        when()
                .delete("/vegetable/" + INVALID_ID)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("code", equalTo(HttpStatus.SC_NOT_FOUND))
                .body("error", equalTo("vegetable '" + INVALID_ID + "' not found"));
    }
}
