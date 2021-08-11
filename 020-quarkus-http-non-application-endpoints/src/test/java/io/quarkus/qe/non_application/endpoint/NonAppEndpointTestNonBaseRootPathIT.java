package io.quarkus.qe.non_application.endpoint;

import static io.quarkus.qe.non_application.endpoint.CommonNonAppEndpoint.NATIVE;
import static io.quarkus.qe.non_application.endpoint.CommonNonAppEndpoint.QUARKUS_PROFILE;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@Disabled(value = "Due to high execution time to build app on Native")
@EnabledIfSystemProperty(named = QUARKUS_PROFILE, matches = NATIVE)
public class NonAppEndpointTestNonBaseRootPathIT extends NonAppEndpointTestNonBaseRootPathTest {
}
