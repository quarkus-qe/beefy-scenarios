package io.quarkus.qe;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.QuarkusIntegrationTest;

@Disabled("Annotation @ClientHeaderParam not working in Native. Reported by https://github.com/quarkusio/quarkus/issues/13660")
@QuarkusIntegrationTest
public class NativeLookupAuthorizationRestPingPongResourceIT extends LookupAuthorizationRestPingPongResourceTest {
}