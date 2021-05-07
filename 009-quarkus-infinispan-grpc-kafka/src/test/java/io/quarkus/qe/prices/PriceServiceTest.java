package io.quarkus.qe.prices;

import static io.restassured.RestAssured.get;

import java.time.Duration;

import org.apache.http.HttpStatus;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.containers.ConfluentKafkaTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(ConfluentKafkaTestResource.class)
public class PriceServiceTest {

    @Test
    public void testPricesResource() {
        Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            get("/prices/poll")
                    .then()
                    .statusCode(HttpStatus.SC_OK);
        });

    }
}
