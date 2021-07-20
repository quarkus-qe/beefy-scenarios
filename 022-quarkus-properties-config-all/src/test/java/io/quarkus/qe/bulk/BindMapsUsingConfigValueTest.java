package io.quarkus.qe.bulk;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BindMapsUsingConfigValueTest {

    @Test
    public void shouldInjectMapsWithStringAsKeyIntoConfigValue() {
        assertResponseIs("/labels/A", "X");
        assertResponseIs("/labels/B", "Y");
    }

    @Test
    public void shouldInjectMapsWithIntegerAsKeyIntoConfigValue() {
        assertResponseIs("/numbers/1", "1");
        assertResponseIs("/numbers/2", "2");
    }

    private <T> void assertResponseIs(String path, T expected) {
        assertResponse(path, is(expected.toString()));
    }

    private void assertResponse(String path, Matcher<String> matcher) {
        given().when().get("/bind-maps-using-config-value" + path)
                .then().statusCode(HttpStatus.SC_OK)
                .body(matcher);
    }
}
