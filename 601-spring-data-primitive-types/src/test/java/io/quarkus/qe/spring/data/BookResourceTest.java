package io.quarkus.qe.spring.data;

import io.quarkus.qe.spring.data.model.Book;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.Matchers.empty;

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

    // QUARKUS-525
    @Test
    void testFindBooksByPublisherZipCode() {
        Response response = when().get("/book/publisher/zipcode/28080").then()
                .statusCode(200).contentType(ContentType.JSON).extract().response();

        List<Book> books = Arrays.asList(response.getBody().as(Book[].class));
        assertThat(books, is(not(empty())));
        books.stream().forEach(book -> assertThat(book.getPublisherAddress().getZipCode(), is("28080")));
    }
}
