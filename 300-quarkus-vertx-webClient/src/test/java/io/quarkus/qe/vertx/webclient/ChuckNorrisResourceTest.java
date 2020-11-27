package io.quarkus.qe.vertx.webclient;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;

@QuarkusTest
@QuarkusTestResource(WireMockChuckNorrisResource.class)
public class ChuckNorrisResourceTest {

    final static String EXPECTED_ID = "aBanNLDwR-SAz7iMHuCiyw";
    final static String EXPECTED_VALUE = "Chuck Norris has already been to mars; that why there's no signs of life";

    @Test
    @DisplayName("Vert.x WebClient [flavor: mutiny] -> Map json response body to POJO")
    public void getChuckJokeAsJSON(){

        stubFor(get(urlEqualTo("/jokes/random"))
                .willReturn(aResponse()
                        .withHeader("Accept", "application/json")
                        .withBody(String.format("{\"categories\":[]," +
                                "\"created_at\":\"2020-01-05 13:42:19.576875\"," +
                                "\"icon_url\":\"https://assets.chucknorris.host/img/avatar/chuck-norris.png\"," +
                                "\"id\":\"%s\"," +
                                "\"updated_at\":\"2020-01-05 13:42:19.576875\"," +
                                "\"url\":\"https://api.chucknorris.io/jokes/sC09X1xQQymE4SciIjyV0g\"," +
                                "\"value\":\"%s\"}", EXPECTED_ID, EXPECTED_VALUE))));

        given()
                .when()
                .get("/chuck")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(containsStringIgnoringCase((String.format("{\"id\":\"%s\",\"jokeText\":\"%s\"}", EXPECTED_ID, EXPECTED_VALUE))));
    }

    @Test
    @DisplayName("Vert.x WebClient [flavor: mutiny] -> Mapped json response by 'as' mutiny method.")
    public void getChuckJokeByJsonBodyCodec() throws InterruptedException {

        stubFor(get(urlEqualTo("/jokes/random"))
                .willReturn(aResponse()
                        .withHeader("Accept", "application/json")
                        .withBody(String.format("{\"categories\":[]," +
                                "\"created_at\":\"2020-01-05 13:42:19.576875\"," +
                                "\"icon_url\":\"https://assets.chucknorris.host/img/avatar/chuck-norris.png\"," +
                                "\"id\":\"%s\"," +
                                "\"updated_at\":\"2020-01-05 13:42:19.576875\"," +
                                "\"url\":\"https://api.chucknorris.io/jokes/sC09X1xQQymE4SciIjyV0g\"," +
                                "\"value\":\"%s\"}", EXPECTED_ID, EXPECTED_VALUE))));

        given()
                .filter(
                        (request, response, ctx) -> {
                            io.restassured.response.Response resp = ctx.next(request, response);
                            if (resp.statusCode() >= 400) {
                                System.err.println(resp.body().prettyPrint());
                                System.err.println(request.getMethod() + " " + request.getURI() + " => "
                                        + response.getStatusCode() + " " + response.getStatusLine());
                            }
                            return resp;
                        })
                .when()
                .get("/chuck/bodyCodec")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(containsStringIgnoringCase((String.format("{\"id\":\"%s\",\"jokeText\":\"%s\"}", EXPECTED_ID, EXPECTED_VALUE))));
    }

    @Test
    @DisplayName("Vert.x WebClient [flavor: mutiny] -> If third party server exceed http client timeout, then throw a timeout exception.")
    public void getTimeoutWhenResponseItsTooSlow(){
        final int delay = 3500; // must be greater than vertx.webclient.timeout-sec

        stubFor(get(urlEqualTo("/jokes/random"))
                .willReturn(aResponse()
                        .withHeader("Accept", "application/json")
                        .withFixedDelay(delay)));

        given()
                .when()
                .get("/chuck/bodyCodec")
                .then()
                .statusCode(Response.Status.REQUEST_TIMEOUT.getStatusCode());
    }
}
