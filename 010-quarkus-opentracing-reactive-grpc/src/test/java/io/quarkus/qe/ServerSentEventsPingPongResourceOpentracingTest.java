package io.quarkus.qe;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@Disabled("https://github.com/quarkusio/quarkus/issues/30935")
public class ServerSentEventsPingPongResourceOpentracingTest extends AbstractPingPongResourceTest {

    @Override
    protected String endpointPrefix() {
        return "server-sent-events";
    }
}
