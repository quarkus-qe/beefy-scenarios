package io.quarkus.qe.prices;

import static io.restassured.RestAssured.get;

import java.time.Duration;

import org.apache.http.HttpStatus;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.containers.KafkaTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
public class PriceServiceTest {

    private static final int ASSERT_TIMEOUT_SECONDS = 5;

    @Test
    public void testPricesResource() {
        Awaitility.await().atMost(Duration.ofSeconds(ASSERT_TIMEOUT_SECONDS)).untilAsserted(() ->
                get("/prices/poll")
                        .then()
                        .statusCode(HttpStatus.SC_OK)
        );
    }
}
