package io.quarkus.qe;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.NativeImageTest;

@Disabled("TODO: Can't use both OIDC Client Filter and OIDC Token extensions. Reported in https://github.com/quarkusio/quarkus/issues/14466")
@NativeImageTest
public class NativeTokenPropagationPingPongResourceIT extends TokenPropagationPingPongResourceTest {
}
