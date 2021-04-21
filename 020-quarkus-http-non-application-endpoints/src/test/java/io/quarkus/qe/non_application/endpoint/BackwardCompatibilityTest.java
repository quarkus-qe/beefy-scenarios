package io.quarkus.qe.non_application.endpoint;

import static io.quarkus.qe.non_application.endpoint.CommonNonAppEndpoint.IS_NATIVE;
import static io.quarkus.qe.non_application.endpoint.CommonNonAppEndpoint.NON_APP_ENDPOINTS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.in;

import java.util.Collections;

import org.apache.http.HttpStatus;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.qe.http.non_application.endpoint.HelloResource;
import io.quarkus.test.QuarkusProdModeTest;

public class BackwardCompatibilityTest {
    private static final String BASE_PATH = "/q";
    private static final String ROOT_PATH = "/api";

    @RegisterExtension
    static final QuarkusProdModeTest BACKWARD_SCENARIO = new QuarkusProdModeTest()
            .setBuildNative(IS_NATIVE)
            .overrideConfigKey("quarkus.http.root-path", ROOT_PATH)
            .overrideConfigKey("quarkus.http.non-application-root-path", BASE_PATH)
            .overrideConfigKey("quarkus.http.redirect-to-non-application-root-path", "true")
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClass(HelloResource.class))
            .setRun(true);

    @Test
    @DisplayName("Non-application relative path")
    public void nonAppEndpointsWithRootPathAndNonAppRootPath() {
        for (String endpoint : NON_APP_ENDPOINTS) {
            given().redirects().follow(false)
                    .log().uri()
                    .expect().statusCode(HttpStatus.SC_MOVED_PERMANENTLY)
                    .header("Location", endsWith(BASE_PATH + endpoint))
                    .when()
                    .get(ROOT_PATH + endpoint);

            given().expect().statusCode(in(Collections.singletonList(HttpStatus.SC_OK))).when().get(ROOT_PATH + endpoint);
        }
    }
}
