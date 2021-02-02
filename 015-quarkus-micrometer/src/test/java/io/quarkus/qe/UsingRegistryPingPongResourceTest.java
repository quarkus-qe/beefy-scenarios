package io.quarkus.qe;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class UsingRegistryPingPongResourceTest {

    private static final String PING_PONG_ENDPOINT = "/using-registry-pingpong/";
    private static final String SIMPLE_SCENARIO = "simple_registry";
    private static final String FORLOOP_SCENARIO = "forloop";
    private static final String FORLOOP_PARALLEL_SCENARIO = "forloopparallel";
    private static final String COUNTER_FORMAT = "%s_total %s.0";
    private static final String NO_QUERY_PARAMS = null;

    private String currentScenario;

    @Test
    public void testSimpleScenarioShouldReturnCountOne() {
        whenCallPingPongScenario(SIMPLE_SCENARIO);
        thenCounterIs(1);
    }

    @Test
    public void testIterativeScenarioShouldReturnCountOne() {
        whenCallPingPongScenario(FORLOOP_SCENARIO, countQuery(50));
        thenCounterIs(50);
    }

    @Test
    public void testIterativeParallelScenarioShouldReturnCountOne() {
        whenCallPingPongScenario(FORLOOP_PARALLEL_SCENARIO, countQuery(500));
        thenCounterIs(500);
    }

    private void whenCallPingPongScenario(String scenario) {
        whenCallPingPongScenario(scenario, NO_QUERY_PARAMS);
    }

    private void whenCallPingPongScenario(String scenario, String queryParams) {
        currentScenario = scenario;
        String path = PING_PONG_ENDPOINT + scenario;
        if (queryParams != null) {
            path += "?" + queryParams;
        }

        given()
                .when().get(path)
                .then().statusCode(HttpStatus.SC_OK)
                .body(is("ping pong"));
    }

    private void thenCounterIs(int expectedCounter) {
        when().get("/q/metrics").then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString(String.format(COUNTER_FORMAT, currentScenario, expectedCounter)));
    }

    private static final String countQuery(int count) {
        return "count=" + count;
    }
}
