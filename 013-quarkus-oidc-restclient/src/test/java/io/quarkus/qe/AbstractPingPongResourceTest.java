package io.quarkus.qe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.keycloak.authorization.client.AuthzClient;

import io.quarkus.qe.containers.KeycloakTestResource;
import io.quarkus.test.common.QuarkusTestResource;

@QuarkusTestResource(KeycloakTestResource.class)
public abstract class AbstractPingPongResourceTest {

    private static final String PING_ENDPOINT = "/%s-ping";
    private static final String PONG_ENDPOINT = "/%s-pong";
    private static final String USER = "test-user";
    private static final String WRONG_TOKEN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    AuthzClient authzClient;

    @Test
    public void testPingUnauthorized() {
        given()
                .when().get(pingEndpoint())
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void testPingUnauthorizedWithWrongToken() {
        given().auth().oauth2(WRONG_TOKEN)
                .when().get(pingEndpoint())
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void testPongUnauthorized() {
        given()
                .when().get(pongEndpoint())
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void testPongUnauthorizedWithWrongToken() {
        given().auth().oauth2(WRONG_TOKEN)
                .when().get(pongEndpoint())
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void testPongIsAuthorized() {
        given().auth().oauth2(createToken())
                .when().get(pongEndpoint())
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testPingPong() {
        // When calling ping, the rest will invoke also the pong rest endpoint.
        given()
                .auth().oauth2(createToken())
                .when().get(pingEndpoint())
                .then().statusCode(HttpStatus.SC_OK)
                .body(is("ping pong"));
    }

    protected abstract String endpointPrefix();

    protected String pingEndpoint() {
        return String.format(PING_ENDPOINT, endpointPrefix());
    }

    protected String pongEndpoint() {
        return String.format(PONG_ENDPOINT, endpointPrefix());
    }

    private String createToken() {
        return authzClient.obtainAccessToken(USER, USER).getToken();
    }
}
