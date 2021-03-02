package io.quarkus.qe;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class RestPingPongResourceOidcTest extends AbstractPingPongResourceTest {

    @Override
    protected String endpointPrefix() {
        return "rest";
    }
}
