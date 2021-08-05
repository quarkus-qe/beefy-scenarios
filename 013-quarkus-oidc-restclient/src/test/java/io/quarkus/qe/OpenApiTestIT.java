package io.quarkus.qe;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class OpenApiTestIT extends OpenApiTest {
    // quarkus-maven-plugin:build adjusts OpenAPI info
    // see https://github.com/quarkusio/quarkus/pull/19148/commits/8fe04d7a06aa976e0b85d72864578f3ab834a27a
    // surefire is executed before quarkus-maven-plugin:build and thus the message there is the generic one
    private static final String QUARKUS_ADJUSTED_INFO = "{\"title\":\"013-quarkus-oidc-restclient API\",\"version\":\"1.0.0-SNAPSHOT\"}";

    @Override
    String getExpectedInfo() {
        return QUARKUS_ADJUSTED_INFO;
    }
}
