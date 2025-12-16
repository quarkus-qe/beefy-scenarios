package io.quarkus.qe.core;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class FallbackResourceTest {

    private static final String WORK_METHOD = "work";
    private static final String FAIL_METHOD = "fail";
    private static final String FALLBACK_METRIC = "ft_invocations_total{fallback=\"%s\",method=\"io.quarkus.qe.core.FallbackResource.%s\",result=\"valueReturned\"} %s.0";
    private static final String FALLBACK_APPLIED = "applied";
    private static final String FALLBACK_NOT_APPLIED = "notApplied";
    private static final String ONE = "1";
    private static final String ZERO = "0";

    @Test
    public void shouldReturnWorked() {
        given()
                .when().get("/fallback/work")
                .then()
                .statusCode(200)
                .body(is(FallbackResource.WORKED));
        assertFallbackWasNotCalledOn(WORK_METHOD);
    }

    @Test
    public void shouldReturnFailed() {
        given()
                .when().get("/fallback/fail")
                .then()
                .statusCode(200)
                .body(is(FallbackResource.FAILED));
        assertFallbackWasCalledOn(FAIL_METHOD);
    }

    private void assertFallbackWasCalledOn(String method) {
        assertFallbackInvocationsOn(method, ZERO, ONE);
    }

    private void assertFallbackWasNotCalledOn(String method) {
        assertFallbackInvocationsOn(method, ONE, ZERO);
    }

    private void assertFallbackInvocationsOn(String method, String expectNotAppliedInvocations,
            String expectAppliedInvocations) {
        String expectedFallbackNotAppliedInvocationsCount = String.format(FALLBACK_METRIC, FALLBACK_NOT_APPLIED, method,
                expectNotAppliedInvocations);
        String expectedFallbackAppliedInvocationsCount = String.format(FALLBACK_METRIC, FALLBACK_APPLIED, method,
                expectAppliedInvocations);
        given()
                .when().get("/q/metrics")
                .then()
                .statusCode(200)
                .body(containsString(expectedFallbackNotAppliedInvocationsCount))
                .body(containsString(expectedFallbackAppliedInvocationsCount));
    }
}
