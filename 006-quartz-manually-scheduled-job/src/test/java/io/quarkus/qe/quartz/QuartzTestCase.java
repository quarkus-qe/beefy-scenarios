package io.quarkus.qe.quartz;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class QuartzTestCase {

    private static final int WAIT_MILLIS = 1000;

    @Test
    public void testAnnotationScheduledCounter() throws InterruptedException {
        Thread.sleep(WAIT_MILLIS);
        assertCounter("/scheduler/count/annotation", 0);
        Thread.sleep(WAIT_MILLIS);
        assertCounter("/scheduler/count/annotation", 1);
    }

    @Test
    public void testManuallyScheduledCounter() throws InterruptedException {
        Thread.sleep(WAIT_MILLIS);
        assertCounter("/scheduler/count/manual", 0);
        Thread.sleep(WAIT_MILLIS);
        assertCounter("/scheduler/count/manual", 1);
    }

    private void assertCounter(String counterPath, int expectedCount) {
        String body = given()
                .when().get(counterPath)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().asString();

        int actualCounter = Integer.valueOf(body);

        assertTrue(actualCounter > expectedCount,
                "Actual counter '" + actualCounter + "' must be greater than the expected '" + expectedCount + "'");
    }

}
