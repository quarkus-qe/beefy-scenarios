package io.quarkus.qe.quartz;

import static io.restassured.RestAssured.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.Duration;

import org.apache.http.HttpStatus;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.qe.quartz.resources.QuartzNodeApplicationResource;
import io.quarkus.qe.quartz.resources.RestApplicationResource;
import io.quarkus.test.QuarkusProdModeTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.restassured.RestAssured;

@QuarkusTestResource(H2DatabaseTestResource.class)
public class AnnotationScheduledJobsQuartzTest {

    private static final int REST_PORT = 8081;
    private static final String NODE_ONE_NAME = "node-one";
    private static final String NODE_TWO_NAME = "node-two";
    private static final int ASSERT_TIMEOUT_SECONDS = 30;

    @RegisterExtension
    static final QuarkusProdModeTest NODE_ONE_APP = new QuartzNodeApplicationResource(NODE_ONE_NAME);

    @RegisterExtension
    static final QuarkusProdModeTest NODE_TWO_APP = new QuartzNodeApplicationResource(NODE_TWO_NAME);

    @RegisterExtension
    static final QuarkusProdModeTest REST_APP = new RestApplicationResource(REST_PORT);

    @BeforeAll
    public static void beforeAll() {
        RestAssured.port = REST_PORT;
    }

    @Test
    public void testClusteringEnvironmentWithUniqueJobs() throws Exception {
        whenBothNodesAreUpAndRunning();
        thenJobIsExecutedWithOwner(NODE_ONE_NAME);

        whenShutdownNodeOne();
        thenJobIsExecutedWithOwner(NODE_TWO_NAME);
    }

    private void whenBothNodesAreUpAndRunning() {
        assertFalse(NODE_ONE_APP.getStartupConsoleOutput().isEmpty(), "Node One should be up and running");
        assertFalse(NODE_TWO_APP.getStartupConsoleOutput().isEmpty(), "Node Two should be up and running");
    }

    private void whenShutdownNodeOne() {
        NODE_ONE_APP.stop();
    }

    private void thenJobIsExecutedWithOwner(String expectedOwner) {
        RestAssured.port = REST_PORT;

        Awaitility.await().atMost(Duration.ofSeconds(ASSERT_TIMEOUT_SECONDS)).untilAsserted(() -> {
            ExecutionEntity[] executions = get("/executions")
                    .then().statusCode(HttpStatus.SC_OK)
                    .extract().as(ExecutionEntity[].class);

            assertEquals(expectedOwner, executions[executions.length - 1].owner, "Expected owner not found");
        });
    }
}
