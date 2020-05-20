package io.quarkus.qe.quartz;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;

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

    private void assertCounter(String counterPath, int minCount) {
        Response response = given().when().get(counterPath);
        String body = response.asString();
        int count = Integer.valueOf(body);
        assertTrue(count > minCount);
        response
                .then()
                .statusCode(200);
    }

}
