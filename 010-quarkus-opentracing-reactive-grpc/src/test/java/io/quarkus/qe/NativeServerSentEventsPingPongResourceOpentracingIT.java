package io.quarkus.qe;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
@Disabled("https://github.com/quarkusio/quarkus/issues/30935")
public class NativeServerSentEventsPingPongResourceOpentracingIT extends ServerSentEventsPingPongResourceOpentracingTest {

}
