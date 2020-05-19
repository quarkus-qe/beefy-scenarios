package io.quarkus.qe.spring.data;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.core.Is.is;

@QuarkusTest
public class BookResourceTest {

    @Test
    void testCustomFindPublicationYearPrimitiveInteger() {
        when().get("/book/customPublicationYearPrimitive/1").then()
                .statusCode(200)
                .body(is("2011"));
    }

    @Test
    void testCustomFindPublicationYearObjectInteger() {
        when().get("/book/customPublicationYearObject/1").then()
                .statusCode(200)
                .body(is("2011"));
    }

    @Test
    void testCustomFindPublicationIsbnPrimitiveLong() {
        when().get("/book/customPublicationIsbnPrimitive/2").then()
                .statusCode(200)
                .body(is("9789295055026"));
    }

    @Test
    void testCustomFindPublicationIsbnObjectLong() {
        when().get("/book/customPublicationIsbnObject/2").then()
                .statusCode(200)
                .body(is("9789295055026"));
    }
}
