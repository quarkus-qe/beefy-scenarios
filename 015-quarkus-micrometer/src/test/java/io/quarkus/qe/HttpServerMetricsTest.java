package io.quarkus.qe;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class HttpServerMetricsTest {

    private static final List<String> HTTP_SERVER_REQUESTS_METRICS_SUFFIX = Arrays.asList("count", "sum", "max");

    private static final String HTTP_SERVER_REQUESTS_METRICS_FORMAT = "http_server_requests_seconds_%s{method=\"GET\",outcome=\"SUCCESS\",status=\"200\",uri=\"%s\",}";
    private static final String PING_PONG_ENDPOINT = "/without-metrics-pingpong";

    @Test
    public void testHttpServerRequestsCountShouldBeRegistered() {
        thenHttpServerRequestsMetricsAreNotPresent();

        whenCallPingPong();
        thenHttpServerRequestsMetricsArePresent();
    }

    private void whenCallPingPong() {
        given()
                .when().get(PING_PONG_ENDPOINT)
                .then().statusCode(HttpStatus.SC_OK)
                .body(is("ping pong"));
    }

    private void thenHttpServerRequestsMetricsAreNotPresent() {
        String metrics = when().get("/q/metrics").then()
                .statusCode(HttpStatus.SC_OK).extract().asString();

        for (String metricSuffix : HTTP_SERVER_REQUESTS_METRICS_SUFFIX) {
            String metric = String.format(HTTP_SERVER_REQUESTS_METRICS_FORMAT, metricSuffix, PING_PONG_ENDPOINT);
            assertFalse(metrics.contains(metric), "Unexpected metric found: " + metric + ". Metrics: " + metrics);
        }
    }

    private void thenHttpServerRequestsMetricsArePresent() {
        String metrics = when().get("/q/metrics").then()
                .statusCode(HttpStatus.SC_OK).extract().asString();

        for (String metricSuffix : HTTP_SERVER_REQUESTS_METRICS_SUFFIX) {
            String metric = String.format(HTTP_SERVER_REQUESTS_METRICS_FORMAT, metricSuffix, PING_PONG_ENDPOINT);
            assertTrue(metrics.contains(metric), "Expected metric not found: " + metric + ". Metrics: " + metrics);
        }
    }
}
