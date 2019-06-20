package io.quarkus.qe.hello;

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
public class StaticResourceTest {

    @TestHTTPResource("big-file")
    URL bigFileURL;

    @Test
    public void testMainPage() {
        given()
                .when().get("/")
                .then()
                .statusCode(200)
                .body(is("Howdy!"));
    }

    @Test
    public void testBigFile() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) bigFileURL.openConnection();
        connection.setRequestMethod("HEAD");
        connection.setConnectTimeout(2000);
        connection.connect();
        int responseCode = connection.getResponseCode();
        connection.disconnect();
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);
    }
}