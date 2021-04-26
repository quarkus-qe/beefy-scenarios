package io.quarkus.qe.spring.data;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.quarkus.qe.spring.data.model.Book;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

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

    //This is for regression test for https://github.com/quarkusio/quarkus/pull/13015
    @Test
    void testFindBooksByPublisherZipCode() {
        List<Book> books = retrieveBooksByZipcode();
        books.stream().forEach(book -> assertThat(book.getPublisherAddress().getZipCode(), is("28080")));
    }

    //This is for regression test for https://github.com/quarkusio/quarkus/issues/13234
    @Test
    void testJpaFieldsMapping() {
        Book book = retrieveBooksByZipcode().stream().findFirst().get();

        // Post a new comment on an existing book
        Map<String, String> comment = Collections.singletonMap("Shakespeare", "Lorem Ipsum Lorem Ipsum");
        book.setComments(comment);
        book = updateBook(book);

        assertThat(book.getComments().size(), equalTo(1));
        assertThat(book.getComments(), equalTo(comment));

        // Update the previous comment
        Map<String, String> commentUpdated = Collections.singletonMap("Shakespeare", "Lorem Ipsum Lorem Ipsum novus");
        book.setComments(commentUpdated);
        book = updateBook(book);
        assertThat(book.getComments().size(), equalTo(1));
        assertThat(book.getComments(), equalTo(commentUpdated));
    }

    private Book updateBook(Book book) {
        return given().contentType(ContentType.JSON).body(book).when().put("/book/" + book.getBid()).then()
                .statusCode(200).contentType(ContentType.JSON).extract().response().getBody().as(Book.class);
    }

    private List<Book> retrieveBooksByZipcode() {
        Response response = when().get("/book/publisher/zipcode/28080").then()
                .statusCode(200).contentType(ContentType.JSON).extract().response();

        List<Book> books = Arrays.asList(response.getBody().as(Book[].class));
        assertThat(books, is(not(empty())));
        return books;
    }
}
