package io.quarkus.qe;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ServerSentEventsPingPongResourceOpentracingTest extends AbstractPingPongResourceTest {

    @Override
    protected String endpointPrefix() {
        return "server-sent-events";
    }
}
