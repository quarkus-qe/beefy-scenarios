package io.quarkus.qe;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.NativeImageTest;

@Disabled("Annotation @ClientHeaderParam not working in Native. Reported by https://github.com/quarkusio/quarkus/issues/13660")
@NativeImageTest
public class NativeLookupAuthorizationRestPingPongResourceIT extends LookupAuthorizationRestPingPongResourceTest {
}