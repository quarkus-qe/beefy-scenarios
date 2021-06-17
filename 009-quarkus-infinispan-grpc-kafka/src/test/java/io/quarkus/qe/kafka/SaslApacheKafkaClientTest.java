package io.quarkus.qe.kafka;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.StringContains.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.qe.containers.SaslKafkaTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(SaslKafkaTestResource.class)
public class SaslApacheKafkaClientTest {
    @Test
    void testKafkaClientSASL() {
        await().untilAsserted(() -> {
            given()
                    .queryParam("key", "my-key")
                    .queryParam("value", "my-value")
                    .when()
                    .post("/kafka/sasl")
                    .then()
                    .statusCode(200);

            get("/kafka/sasl")
                    .then()
                    .statusCode(200)
                    .body(containsString("my-key-my-value"));
        });

        get("/kafka/sasl/topics")
                .then()
                .statusCode(200)
                .body(containsString("hello"));
    }
}
