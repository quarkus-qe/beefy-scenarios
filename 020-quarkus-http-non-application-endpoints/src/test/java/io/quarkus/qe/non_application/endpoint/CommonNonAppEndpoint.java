package io.quarkus.qe.non_application.endpoint;

import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public abstract class CommonNonAppEndpoint {
    protected static final String ROOT_BASE_PATH = "/api/";
    protected static final String QUARKUS_PROFILE = "quarkus.profile";
    protected static final String NATIVE = "native";
    public static final boolean IS_NATIVE = System.getProperty(QUARKUS_PROFILE, "").equals(NATIVE);
    private RequestSpecification request;
    private RequestSpecBuilder spec;

    protected static final List<String> nonAppEndpoints = Arrays.asList(
            "/openapi", "/metrics/base", "/metrics/application",
            "/metrics/vendor", "/metrics", "/health/group", "/health/well", "/health/ready",
            "/health/live", "/health", "/swagger-ui");

    protected void givenBasePath(String basePath) {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        this.spec = new RequestSpecBuilder();
        this.spec.setBasePath(basePath);
    }

    protected void whenMakeRequestOverNonAppEndpoints() {
        this.request = spec.build();
    }

    protected void thenStatusCodeShouldBe(int status) {
        for (String endpoint : nonAppEndpoints) {
            given().spec(request).log().uri().expect().statusCode(status).when().get(endpoint);
        }
    }

    @Test
    protected void nonAppEndpointScenario() {
        givenBasePath(getBasePath());
        whenMakeRequestOverNonAppEndpoints();
        thenStatusCodeShouldBe(getExpectedHttpStatus());
    }

    public abstract int getExpectedHttpStatus();

    public abstract String getBasePath();
}
