package io.quarkus.qe;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TokenPropagationPingPongResourceTest extends AbstractPingPongResourceTest {

    @Override
    protected String endpointPrefix() {
        return "token-propagation";
    }

    /**
     * This scenario is reusing the same rest endpoint for the Pong service.
     */
    @Override
    protected String pongEndpoint() {
        return "/rest-pong";
    }
}
