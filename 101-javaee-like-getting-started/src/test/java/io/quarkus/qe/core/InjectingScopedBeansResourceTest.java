package io.quarkus.qe.core;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class InjectingScopedBeansResourceTest {
    @Test
    public void shouldInjectScopedBeans() {
        given().when().get("/scopedbeans/sessionId")
                .then().body(notNullValue());

        given().when().get("/scopedbeans/requestId")
                .then().body(notNullValue());

        given().get("/scopedbeans/contextPath")
                .then().body(notNullValue());
    }
}
