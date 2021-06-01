package io.quarkus.qe.non_application.endpoint;

import static io.quarkus.qe.non_application.endpoint.CommonNonAppEndpoint.IS_NATIVE;
import static io.restassured.RestAssured.when;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.qe.http.non_application.endpoint.HelloResource;
import io.quarkus.test.QuarkusProdModeTest;

public class LegacyNonApplicationEndpointTest {

    @RegisterExtension
    static final QuarkusProdModeTest nonApplicationEndpointScenario = new QuarkusProdModeTest()
            .setBuildNative(IS_NATIVE)
            .overrideConfigKey("quarkus.http.root-path", "/api")
            .overrideConfigKey("quarkus.smallrye-health.root-path", "/health")
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClass(HelloResource.class))
            .setRun(true);

    @Test
    protected void nonAppEndpointScenario() {
        when().get("/health").then().statusCode(200);
    }
}
