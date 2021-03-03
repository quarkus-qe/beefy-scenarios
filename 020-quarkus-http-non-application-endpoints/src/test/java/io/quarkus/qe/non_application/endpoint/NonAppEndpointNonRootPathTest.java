package io.quarkus.qe.non_application.endpoint;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.qe.http.non_application.endpoint.HelloResource;
import io.quarkus.test.QuarkusProdModeTest;

public class NonAppEndpointNonRootPathTest extends CommonNonAppEndpoint {
    private static final String BASE_PATH = "/";

    @RegisterExtension
    static final QuarkusProdModeTest nonRootPathScenario = new QuarkusProdModeTest()
            .overrideConfigKey("quarkus.http.root-path", "/api")
            .overrideConfigKey("quarkus.http.non-application-root-path", BASE_PATH)
            .overrideConfigKey("quarkus.http.redirect-to-non-application-root-path", "false")
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClass(HelloResource.class));

    @BeforeEach
    public void beforeEach() {
        nonRootPathScenario.stop();
        nonRootPathScenario.start();
    }

    @Test
    @DisplayName("Non-application endpoint with root-path set to 'api' and non-application-root-path set to '/'")
    public void nonAppEndpointsRootPathSlash() {
        givenBasePath(ROOT_BASE_PATH);
        whenMakeRequestOverNonAppEndpoints();
        thenStatusCodeShouldBe(404);
    }
}
