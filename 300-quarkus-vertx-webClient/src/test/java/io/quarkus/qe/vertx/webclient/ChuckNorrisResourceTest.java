package io.quarkus.qe.vertx.webclient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Every.everyItem;
import static org.hamcrest.core.StringContains.containsString;

import java.net.HttpURLConnection;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.vertx.webclient.resources.JaegerTestResource;
import io.quarkus.qe.vertx.webclient.resources.WireMockChuckNorrisResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;

@QuarkusTest
@QuarkusTestResource(WireMockChuckNorrisResource.class)
@QuarkusTestResource(JaegerTestResource.class)
public class ChuckNorrisResourceTest {
    private final static String jaegerEndpoint = "http://localhost:16686/api/traces";
    static final String EXPECTED_ID = "aBanNLDwR-SAz7iMHuCiyw";
    static final String EXPECTED_VALUE = "Chuck Norris has already been to mars; that why there's no signs of life";
    static final int DELAY = 3500; // must be greater than vertx.webclient.timeout-sec
    static final String QUARKUS_PROFILE = "quarkus.profile";
    static final String NATIVE = "native";
    static final boolean IS_NATIVE = System.getProperty(QUARKUS_PROFILE, "").equals(NATIVE);

    private Response resp;

    @Test
    @DisplayName("Vert.x WebClient [flavor: mutiny] -> Map json response body to POJO")
    public void getChuckJokeAsJSON() {
        setupMockHttpServer();
        given()
                .when()
                .get("/chuck/")
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("id", containsStringIgnoringCase(EXPECTED_ID))
                .body("value", containsStringIgnoringCase(EXPECTED_VALUE));
    }

    @Test
    @DisplayName("Vert.x WebClient [flavor: mutiny] -> Mapped json response by 'as' mutiny method.")
    public void getChuckJokeByJsonBodyCodec() throws InterruptedException {
        setupMockHttpServer();
        given()
                .when()
                .get("/chuck/bodyCodec/")
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("id", containsStringIgnoringCase(EXPECTED_ID))
                .body("value", containsStringIgnoringCase(EXPECTED_VALUE));
    }

    @Test
    @DisplayName("Vert.x WebClient [flavor: mutiny] -> If third party server exceed http client timeout, then throw a timeout exception.")
    public void getTimeoutWhenResponseItsTooSlow() {
        stubFor(get(urlEqualTo("/jokes/random"))
                .willReturn(aResponse()
                        .withHeader("Accept", "application/json")
                        .withFixedDelay(DELAY)));

        given()
                .when()
                .get("/chuck/bodyCodec/")
                .then()
                .statusCode(HttpURLConnection.HTTP_CLIENT_TIMEOUT);
    }

    @Test
    public void endpointShouldTrace() {
        final int pageLimit = 50;
        final String expectedOperationName = "trace/ping";
        await().atMost(1, TimeUnit.MINUTES).pollInterval(Duration.ofSeconds(1)).untilAsserted(() -> {
            whenIMakePingRequest();
            thenRetrieveTraces(pageLimit, "1h", getServiceName(), expectedOperationName);
            thenStatusCodeMustBe(HttpStatus.SC_OK);
            thenTraceDataSizeMustBe(greaterThan(0));
            thenTraceSpanSizeMustBe(greaterThan(0));
            thenTraceSpanTagsSizeMustBe(greaterThan(0));
            thenTraceSpansOperationNameMustBe(not(empty()));
            thenCheckThatAllOperationNamesAreEqualTo(expectedOperationName);
        });
    }

    @Test
    @Disabled("https://github.com/quarkusio/quarkus/issues/16507")
    public void httpClientShouldHaveHisOwnSpan() {
        final int pageLimit = 50;
        final String expectedOperationName = "trace/ping";
        await().atMost(1, TimeUnit.MINUTES).pollInterval(Duration.ofSeconds(1)).untilAsserted(() -> {
            whenIMakePingRequest();
            thenRetrieveTraces(pageLimit, "1h", getServiceName(), expectedOperationName);
            thenStatusCodeMustBe(HttpStatus.SC_OK);
            thenTraceDataSizeMustBe(greaterThan(0));
            thenTraceSpanSizeMustBe(greaterThan(1));
            thenTraceSpanTagsSizeMustBe(greaterThan(0));
            thenTraceSpansOperationNameMustBe(not(empty()));
            thenCheckThatAllOperationNamesAreEqualTo(expectedOperationName);
        });
    }

    private void whenIMakePingRequest() {
        given().when()
                .get("/trace/ping")
                .then()
                .statusCode(HttpStatus.SC_OK).body(containsStringIgnoringCase("ping-pong"));
    }

    private void thenRetrieveTraces(int pageLimit, String lookBack, String serviceName, String operationName) {
        resp = given().when()
                .queryParam("limit", pageLimit)
                .queryParam("lookback", lookBack)
                .queryParam("service", serviceName)
                .queryParam("operation", operationName)
                .get(jaegerEndpoint);
    }

    private void thenStatusCodeMustBe(int expectedStatusCode) {
        resp.then().statusCode(expectedStatusCode);
    }

    private void thenTraceDataSizeMustBe(Matcher<?> matcher) {
        resp.then().body("data.size()", matcher);
    }

    private void thenTraceSpanSizeMustBe(Matcher<?> matcher) {
        resp.then().body("data[0].spans.size()", matcher);
    }

    private void thenTraceSpanTagsSizeMustBe(Matcher<?> matcher) {
        resp.then().body("data[0].spans[0].tags.size()", matcher);
    }

    private void thenTraceSpansOperationNameMustBe(Matcher<?> matcher) {
        resp.then().body("data.spans.operationName", matcher);
    }

    private void thenCheckThatAllOperationNamesAreEqualTo(String expectedOperationName) {
        List<String> operationNames = resp.then().extract().jsonPath().getList("data.spans.operationName", String.class);
        assertThat(operationNames, everyItem(containsString(expectedOperationName)));
    }

    private void setupMockHttpServer() {
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
    }

    private String getServiceName() {
        // TODO https://github.com/quarkusio/quarkus/issues/16499
        return (IS_NATIVE) ? "300-quarkus-vertx-webclient" : "<<unset>>";
    }
}
