package io.quarkus.qe.jbang;

import io.quarkus.qe.resources.JbangContainerResource;
import io.quarkus.qe.resources.WireMockResource;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static io.quarkus.qe.jbang.stockCryptoCurrency.CRYPTO_PATH_ENV;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class QuarkusRestTest {
    static final int TIMEOUT = 300000;
    static final int WIREMOCK_PORT = 8080;
    static final String WIREMOCK_HOST = "172.17.0.1"; // Docker to host
    static JbangContainerResource jbangContainer;
    static WireMockResource cryptoApiMock;
    private static final String SCRIPT = stockCryptoCurrency.class.getCanonicalName().replace(".", "/") + ".java";

    @BeforeAll
    public static void beforeAll() {
        setupRestAssurance(TIMEOUT);
        launchCryptoApiMock();
        launchJbangApp(Collections.singletonMap(CRYPTO_PATH_ENV, cryptoApiMock.getEndpoint()));
    }

    @AfterAll
    public static void tearDown() {
        cryptoApiMock.stop();
        jbangContainer.stop();
    }

    @Test
    public void helloWorld() {
        given().port(jbangContainer.getPort())
                .when()
                .get("/api/stock/hello/World")
                .then()
                .statusCode(200)
                .body("name", is("Hello, World!"));
    }

    @Test
    public void getAllCurrencies() throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File expectedResponse = new File(classLoader.getResource("allCurrenciesExample.json").getFile());
        stubFor(any(urlPathEqualTo("/data/all/coinlist"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(FileUtils.readFileToByteArray(expectedResponse))));

        given()
                .port(jbangContainer.getPort())
                .when()
                .get("/api/stock/currency/names")
                .then()
                .statusCode(200)
                .body("index.size()", Matchers.greaterThan(1000));
    }

    @Test
    public void getCurrency(){
        stubFor(get(urlEqualTo("/data/pricemulti?fsyms=KIN&tsyms=EUR,USD,BTC"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(String.format("{\"KIN\":{\"BTC\":1e-9,\"USD\":0.00004039,\"EUR\":0.00003351}}"))));

        given()
                .port(jbangContainer.getPort())
                .when()
                .get("/api/stock/currency/KIN/value")
                .then()
                .statusCode(200)
                .body("name", Matchers.is("KIN"));
    }

    private static void setupRestAssurance(int timeout) {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.config=RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig().
                setParam("http.connection.timeout",timeout).
                setParam("http.socket.timeout",timeout).
                setParam("http.connection-manager.timeout",timeout));
    }

    private static Map<String, String> launchCryptoApiMock(){
        cryptoApiMock = new WireMockResource();
        cryptoApiMock.setHost(WIREMOCK_HOST);
        cryptoApiMock.setPort(WIREMOCK_PORT);
        return cryptoApiMock.start();
    }

    private static Map<String, String> launchJbangApp(Map<String, String> env) {
        jbangContainer = new JbangContainerResource();
        jbangContainer.setScript(SCRIPT);
        jbangContainer.setEnv(env);
        return jbangContainer.start();
    }
}