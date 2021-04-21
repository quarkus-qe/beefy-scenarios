package io.quarkus.qe.content;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class LargeStaticResourceTest {

    private static final int CONNECT_TIMEOUT = 2000;

    @TestHTTPResource("big-file")
    URL bigFileURL;

    @Test
    public void testMainPageAvailability() {
        given()
                .when().get("/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("Howdy!"));
    }

    @Test
    public void testBigFileAvailability() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) bigFileURL.openConnection();
        connection.setRequestMethod("HEAD");
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.connect();
        int responseCode = connection.getResponseCode();
        connection.disconnect();
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);
    }
}