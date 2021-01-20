package io.quarkus.qe;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class UsingMicroProfilePingPongResourceTest {

    private static final String PING_PONG_ENDPOINT = "/using-microprofile-pingpong";
    private static final String COUNTER_FORMAT = "simple_mp_total{scope=\"application\",} %s.0";

    @Test
    public void testShouldReturnCountOne() {
        whenCallPingPong();
        thenCounterIs(1);
    }

    private void whenCallPingPong() {
        given()
                .when().get(PING_PONG_ENDPOINT)
                .then().statusCode(HttpStatus.SC_OK)
                .body(is("ping pong"));
    }

    private void thenCounterIs(int expectedCounter) {
        when().get("/q/metrics").then()
                .statusCode(200)
                .body(containsString(String.format(COUNTER_FORMAT, expectedCounter)));
    }
}
