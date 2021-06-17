package io.quarkus.qe.kafka;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.StringContains.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.qe.containers.SslStrimziKafkaTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(SslStrimziKafkaTestResource.class)
public class SslApacheKafkaClientTest {
    @Test
    void testKafkaClientSSL() {
        await().untilAsserted(() -> {
            given()
                    .queryParam("key", "my-key")
                    .queryParam("value", "my-value")
                    .when()
                    .post("/kafka/ssl")
                    .then()
                    .statusCode(200);

            get("/kafka/ssl")
                    .then()
                    .statusCode(200)
                    .body(containsString("my-key-my-value"));
        });

        get("/kafka/ssl/topics")
                .then()
                .statusCode(200)
                .body(containsString("hello"));
    }
}
