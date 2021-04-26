package io.quarkus.qe.bulk;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BulkOfPropertiesTest {

    private static final String HTTP = "http://";
    private static final String EXPECTED_EXAMPLE_HOST = "example.org";
    private static final String EXPECTED_DEFAULT_HOST = "default.org";
    private static final String EXPECTED_ENDPOINT_PATH = "/endpoint";
    private static final String EXPECTED_CUSTOM_PATH = "/mypath";
    private static final String EXPECTED_PORT = ":8080";
    private static final String EXPECTED_SERVER_URL = HTTP + EXPECTED_EXAMPLE_HOST + EXPECTED_ENDPOINT_PATH;
    private static final String EXPECTED_SERVER_HOST_RAW = "${server.host}";

    @Test
    public void shouldInjectBulkOfSimpleProperties() {
        assertResponseIs("/url", EXPECTED_SERVER_URL);
        assertResponseIs("/port", 8080);
        assertResponseIs("/repeatedPort", 8080);
        assertResponseIs("/path", "mypath");
    }

    @Test
    public void shouldInjectBulkOfDefaultProperties() {
        assertResponseIs("/urlWithDefaultAndConfigFound", EXPECTED_SERVER_URL);
        assertResponseIs("/urlWithDefaultAndConfigNotFound", HTTP + EXPECTED_DEFAULT_HOST + EXPECTED_ENDPOINT_PATH);
        assertResponseIs("/urlWithDefaultNested", EXPECTED_SERVER_URL);
    }

    @Test
    public void shouldInjectBulkOfComposedProperties() {
        assertResponseIs("/urlComposed", HTTP + EXPECTED_EXAMPLE_HOST + EXPECTED_PORT + EXPECTED_CUSTOM_PATH);
    }

    @Test
    public void shouldInjectBulkOfRawProperties() {
        assertResponseIs("/urlRaw", EXPECTED_SERVER_HOST_RAW);
    }

    private <T> void assertResponseIs(String path, T expected) {
        given().when().get("/bulk-properties" + path)
                .then().statusCode(HttpStatus.SC_OK)
                .body(is(expected.toString()));
    }
}
