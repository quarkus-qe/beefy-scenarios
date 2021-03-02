package io.quarkus.qe;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class RestPingPongResourceOpentracingTest extends AbstractPingPongResourceTest {

    @Override
    protected String endpointPrefix() {
        return "rest";
    }
}
