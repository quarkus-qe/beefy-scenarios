package io.quarkus.qe.non_application.endpoint;

import static io.quarkus.qe.non_application.endpoint.CommonNonAppEndpoint.NATIVE;
import static io.quarkus.qe.non_application.endpoint.CommonNonAppEndpoint.QUARKUS_PROFILE;


import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@EnabledIfSystemProperty(named = QUARKUS_PROFILE, matches = NATIVE)
public class BackwardCompatibilityIT extends BackwardCompatibilityTest {
}
