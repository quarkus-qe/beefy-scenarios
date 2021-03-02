package io.quarkus.qe;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ReactivePingPongResourceOidcTest extends AbstractPingPongResourceTest {

    @Override
    protected String endpointPrefix() {
        return "reactive";
    }
}
