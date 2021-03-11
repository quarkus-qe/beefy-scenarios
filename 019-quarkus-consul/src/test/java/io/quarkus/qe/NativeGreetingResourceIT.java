package io.quarkus.qe;

import static io.quarkus.qe.GreetingResourceTest.NATIVE;
import static io.quarkus.qe.GreetingResourceTest.QUARKUS_PROFILE;

import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@EnabledIfSystemProperty(named = QUARKUS_PROFILE, matches = NATIVE)
public class NativeGreetingResourceIT extends GreetingResourceTest {
}
