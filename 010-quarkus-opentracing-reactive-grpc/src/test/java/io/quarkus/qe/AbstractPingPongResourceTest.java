package io.quarkus.qe;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.containers.JaegerTestResource;
import io.quarkus.test.common.QuarkusTestResource;

@QuarkusTestResource(JaegerTestResource.class)
public abstract class AbstractPingPongResourceTest {

    private static final String PING_ENDPOINT = "/%s-ping";
    private static final String PONG_ENDPOINT = "/%s-pong";

    private static final String PING_RESOURCE = "PingResource";
    private static final String PONG_RESOURCE = "PongResource";

    private static final int ASSERT_TIMEOUT_SECONDS = 5;

    @ConfigProperty(name = JaegerTestResource.JAEGER_API_ENDPOINT, defaultValue = "http://localhost:16686/api/traces")
    String jaegerEndpoint;

    @Test
    public void testPingPong() {
        // When calling ping, the rest will invoke also the pong rest endpoint.
        given()
                .when().get(pingEndpoint())
                .then().statusCode(HttpStatus.SC_OK)
                .body(containsString("ping pong"));

        // Then both ping and pong rest endpoints should have the same trace Id.
        String pingTraceId = given()
                .when().get(pingEndpoint() + "/lastTraceId")
                .then().statusCode(HttpStatus.SC_OK).and().extract().asString();

        assertTraceIdWithPongService(pingTraceId);

        // Then Jaeger is invoked
        await().atMost(ASSERT_TIMEOUT_SECONDS, TimeUnit.SECONDS).untilAsserted(() -> given()
                .when().get(jaegerEndpoint + "/" + pingTraceId)
                .then().statusCode(HttpStatus.SC_OK)
                .and().body(allOf(containsString(PING_RESOURCE), containsString(PONG_RESOURCE))));
    }

    protected abstract String endpointPrefix();

    protected void assertTraceIdWithPongService(String expected) {
        String pongTraceId = given()
                .when().get(pongEndpoint() + "/lastTraceId")
                .then().statusCode(HttpStatus.SC_OK).and().extract().asString();

        assertEquals(expected, pongTraceId);
    }

    private String pingEndpoint() {
        return String.format(PING_ENDPOINT, endpointPrefix());
    }

    private String pongEndpoint() {
        return String.format(PONG_ENDPOINT, endpointPrefix());
    }
}
