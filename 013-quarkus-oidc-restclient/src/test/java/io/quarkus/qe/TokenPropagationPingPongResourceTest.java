package io.quarkus.qe;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.QuarkusTest;

@Disabled("TODO: Can't use both OIDC Client Filter and OIDC Token extensions. Reported in https://github.com/quarkusio/quarkus/issues/14466")
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
