package io.quarkus.qe.non_application.endpoint;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.qe.http.non_application.endpoint.HelloResource;
import io.quarkus.test.QuarkusProdModeTest;

public class RelativePathNonAppEndpointTest extends CommonNonAppEndpoint {
    private static final String BASE_PATH = "q";

    @RegisterExtension
    static final QuarkusProdModeTest relativePathScenario = new QuarkusProdModeTest()
            .overrideConfigKey("quarkus.http.root-path", "/api")
            .overrideConfigKey("quarkus.http.non-application-root-path", BASE_PATH)
            .overrideConfigKey("quarkus.http.redirect-to-non-application-root-path", "false")
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class).addClass(HelloResource.class))
            .setRun(true);


    @Test
    @DisplayName("Non-application relative path")
    public void nonAppEndpointsWithRootPathAndNonAppRootPath() {
        givenBasePath(ROOT_BASE_PATH + BASE_PATH);
        whenMakeRequestOverNonAppEndpoints();
        thenStatusCodeShouldBe(200);
    }
}
