package io.quarkus.qe;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
@Disabled("https://github.com/quarkusio/quarkus/issues/30935")
public class NativeServerSentEventsPingPongResourceOpentracingIT extends ServerSentEventsPingPongResourceOpentracingTest {

}
