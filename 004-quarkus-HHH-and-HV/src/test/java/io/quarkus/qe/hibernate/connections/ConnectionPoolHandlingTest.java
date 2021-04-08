package io.quarkus.qe.hibernate.connections;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

import java.time.Duration;

import org.apache.http.HttpStatus;
import org.awaitility.Awaitility;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.hibernate.resources.CustomH2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;

/**
 * Reproducer for https://github.com/quarkusio/quarkus/issues/15025.
 */
@QuarkusTest
public class ConnectionPoolHandlingTest {

    private static final String EXPECTED_COUNTER = "vendor_agroal_available_count{datasource=\"default\"} %s.0";

    @ConfigProperty(name = "quarkus.datasource.jdbc.min-size")
    int minConnections;

    CustomH2DatabaseTestResource database;
    Thread restartDatabaseJob;

    @BeforeEach
    public void setup() {
        restartDatabaseJob = new Thread(this::restartDatabaseIndefinitely);
    }

    @AfterEach
    public void tearDown() {
        if (restartDatabaseJob == null) {
            restartDatabaseJob.interrupt();
        }
    }

    @Test
    public void connectionPoolShouldRemoveDeadDatabaseConnections() throws InterruptedException {
        whenRestartDatabaseManyTimes();
        thenVendorAgroalAvailableCountShouldNeverBeUpToMinConnections();
    }

    private void whenRestartDatabaseManyTimes() {
        restartDatabaseJob.start();
    }

    private void thenVendorAgroalAvailableCountShouldNeverBeUpToMinConnections() {
        // Wait 4 seconds to start checking and up to 15 seconds to verify the assertion (as the counter can increase over time)
        Awaitility.await().pollDelay(Duration.ofSeconds(8)).atMost(Duration.ofSeconds(30))
                .untilAsserted(() -> {
                    when().get("/q/metrics").then().statusCode(HttpStatus.SC_OK)
                            .and().body(containsString(String.format(EXPECTED_COUNTER, minConnections)));
        });

    }

    private void restartDatabaseIndefinitely() {
        try {
            while (true) {
                database.stop();
                database.start();
                Thread.sleep(1000);
                // call health check to acquire the new connection
                when().get("/q/health");
            }

        } catch (InterruptedException e) {
            // stopped in tearDown
        }
    }
}
