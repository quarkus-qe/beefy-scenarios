package io.quarkus.qe;

import static io.restassured.RestAssured.given;
import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import java.util.UUID;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.authorization.client.AuthzClient;

import io.quarkus.qe.containers.KeycloakTestResource;
import io.quarkus.qe.model.Score;
import io.quarkus.test.common.QuarkusTestResource;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;

@QuarkusTestResource(KeycloakTestResource.class)
public abstract class AbstractPingPongResourceTest {

    private static final String PING_ENDPOINT = "/%s-ping";
    private static final String PONG_ENDPOINT = "/%s-pong";
    private static final String USER = "test-user";
    private static final String WRONG_TOKEN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String HTTP_SOCKET_TIMEOUT_PROPERTY = "http.socket.timeout";
    private static final String HTTP_CONNECTION_TIMEOUT_PROPERTY = "http.connection.timeout";
    private static final int TIMEOUT_IN_SECONDS = 1000;

    AuthzClient authzClient;

    @BeforeEach
    public void setup() {
        RestAssured.config = RestAssuredConfig.config()
                .httpClient(httpClientConfig()
                        .setParam(HTTP_SOCKET_TIMEOUT_PROPERTY, TIMEOUT_IN_SECONDS)
                        .setParam(HTTP_CONNECTION_TIMEOUT_PROPERTY, TIMEOUT_IN_SECONDS));
    }

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

    @Test
    public void testNotFoundIsAuthorized() {
        given().auth().oauth2(createToken())
                .when().get(pongEndpoint() + "/notFound/id")
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void testNotFoundUnauthorized() {
        given()
                .when().get(pongEndpoint() + "/notFound/id")
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void testPingPongWithPathParam() {
        final String name = "helloWorld";
        given().auth().oauth2(createToken())
                .when()
                .get(pingEndpoint() + "/name/" + name)
                .then().statusCode(HttpStatus.SC_OK).body(containsString("ping pong " + name));
    }

    @Test
    public void testPingPongCreate() {
        Score score = new Score(15, 30);
        given().auth().oauth2(createToken())
                .contentType(ContentType.JSON)
                .body(score)
                .when()
                .post(pingEndpoint() + "/withBody")
                .then().statusCode(HttpStatus.SC_OK).body(containsString("ping -> " + score.toString()));
    }

    @Test
    public void testPingPongUpdate() {
        Score score = new Score(15, 30);
        given().auth().oauth2(createToken())
                .contentType(ContentType.JSON)
                .body(score)
                .when()
                .put(pingEndpoint() + "/withBody")
                .then().statusCode(HttpStatus.SC_OK).body(containsString("ping -> " + score.toString()));
    }

    @Test
    public void testPingPongDelete() {
        given().auth().oauth2(createToken())
                .contentType(ContentType.JSON)
                .when()
                .delete(pingEndpoint() + "/" + UUID.randomUUID().toString())
                .then().statusCode(HttpStatus.SC_OK).body(containsString("ping -> true"));
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
