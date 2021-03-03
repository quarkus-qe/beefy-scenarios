package io.quarkus.qe.non_application.endpoint;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.in;

import java.util.Collections;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.qe.http.non_application.endpoint.HelloResource;
import io.quarkus.test.QuarkusProdModeTest;

public class BackwardCompatibilityTest extends CommonNonAppEndpoint {
    private static final String BASE_PATH = "/q";

    @RegisterExtension
    static final QuarkusProdModeTest backwardScenario = new QuarkusProdModeTest()
            .overrideConfigKey("quarkus.http.root-path", "/api")
            .overrideConfigKey("quarkus.http.non-application-root-path", BASE_PATH)
            .overrideConfigKey("quarkus.http.redirect-to-non-application-root-path", "true")
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClass(HelloResource.class));

    @BeforeEach
    public void beforeEach() {
        backwardScenario.stop();
        backwardScenario.start();
    }

    @Test
    @DisplayName("Non-application relative path")
    public void nonAppEndpointsWithRootPathAndNonAppRootPath() {
        for (String endpoint : nonAppEndpoints) {
            given().redirects().follow(false)
                    .log().uri()
                    .expect().statusCode(301).header("Location", endsWith("/q" + endpoint)).when().get(endpoint);

            given().expect().statusCode(in(Collections.singletonList(200))).when().get(endpoint);
        }
    }
}
