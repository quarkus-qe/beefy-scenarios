package io.quarkus.qe.content;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class LargeStaticResourceTest {

    @TestHTTPResource("big-file")
    URL bigFileURL;

    @Test
    public void testMainPageAvailability() {
        given()
                .when().get("/")
                .then()
                .statusCode(200)
                .body(is("Howdy!"));
    }

    @Test
    public void testBigFileAvailability() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) bigFileURL.openConnection();
        connection.setRequestMethod("HEAD");
        connection.setConnectTimeout(2000);
        connection.connect();
        int responseCode = connection.getResponseCode();
        connection.disconnect();
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);
    }
}