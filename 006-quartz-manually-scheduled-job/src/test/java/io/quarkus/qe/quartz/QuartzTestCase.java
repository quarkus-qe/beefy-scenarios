package io.quarkus.qe.quartz;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class QuartzTestCase {

    @Test
    public void testAnnotationScheduledCounter() throws InterruptedException {
        Thread.sleep(1000);
        assertCounter("/scheduler/count/annotation", 0);
        Thread.sleep(1000);
        assertCounter("/scheduler/count/annotation", 1);
    }

    @Test
    public void testManuallyScheduledCounter() throws InterruptedException {
        Thread.sleep(1000);
        assertCounter("/scheduler/count/manual", 0);
        Thread.sleep(1000);
        assertCounter("/scheduler/count/manual", 1);
    }

    private void assertCounter(String counterPath, int expectedCount) {
        String body = given()
                .when().get(counterPath)
                .then().statusCode(200)
                .extract().asString();

        int actualCounter = Integer.valueOf(body);

        assertTrue(actualCounter > expectedCount,
                "Actual counter '" + actualCounter + "' must be greater than the expected '" + expectedCount + "'");
    }

}
