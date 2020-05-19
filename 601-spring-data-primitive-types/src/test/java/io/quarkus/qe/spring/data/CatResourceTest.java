package io.quarkus.qe.spring.data;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.core.Is.is;

@QuarkusTest
public class CatResourceTest {

    @Test
    void testCustomFindPublicationYearObjectBoolean() {
        when().get("/cat/customFindDistinctiveObject/2").then()
                .statusCode(200)
                .body(is("true"));
    }
    @Test
    void testCustomFindPublicationYearPrimitiveBoolean() {
        when().get("/cat/customFindDistinctivePrimitive/2").then()
                .statusCode(200)
                .body(is("true"));
    }
}
