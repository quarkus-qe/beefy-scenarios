package io.quarkus.qe.books;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@QuarkusTest
public class HomePageTest {
    private static final String APP_NAME = "Bootstrap Spring Boot";

    @Test
    public void shouldQuteReplaceWelcomePhrase() {
        RestAssured.get("/")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.HTML)
                .body(CoreMatchers.containsString(APP_NAME));
    }
}
