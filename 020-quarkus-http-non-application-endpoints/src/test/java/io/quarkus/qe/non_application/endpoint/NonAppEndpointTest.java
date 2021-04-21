package io.quarkus.qe.non_application.endpoint;

import javax.ws.rs.core.Response;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.qe.http.non_application.endpoint.HelloResource;
import io.quarkus.test.QuarkusProdModeTest;

public class NonAppEndpointTest extends CommonNonAppEndpoint {

    private static final String BASE_PATH = "/q";

    @RegisterExtension
    static final QuarkusProdModeTest NON_APPLICATION_ENDPOINT_SCENARIO = new QuarkusProdModeTest()
            .setBuildNative(IS_NATIVE)
            .overrideConfigKey("quarkus.http.root-path", "/api")
            .overrideConfigKey("quarkus.http.non-application-root-path", BASE_PATH)
            .overrideConfigKey("quarkus.http.redirect-to-non-application-root-path", "false")
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClass(HelloResource.class))
            .setRun(true);

    @Override
    public int getExpectedHttpStatus() {
        return Response.Status.OK.getStatusCode();
    }

    @Override
    public String getBasePath() {
        return BASE_PATH;
    }
}
