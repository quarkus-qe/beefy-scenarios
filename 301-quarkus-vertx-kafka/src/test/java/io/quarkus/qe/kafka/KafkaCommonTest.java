package io.quarkus.qe.kafka;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Every.everyItem;
import static org.hamcrest.core.StringContains.containsString;

import io.restassured.response.Response;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;

import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

abstract class KafkaCommonTest {
    private final static String jaegerEndpoint = "http://localhost:16686/api/traces";
    static final String NATIVE = "native";
    static final String QUARKUS_PROFILE = "quarkus.profile";
    static final boolean IS_NATIVE = System.getProperty(QUARKUS_PROFILE, "").equals(NATIVE);

    private Response resp;

    @Test
    public void producerConsumerTest() throws InterruptedException {
        final int timeoutMin = 5;
        final int expectedEvents = 1;
        sendAndReceiveEvents(timeoutMin, expectedEvents);
    }

    @Test
    public void kafkaProducerShouldTrace() {
        final int pageLimit = 50;
        final String expectedOperationName = "stock-price send";
        await().atMost(1, TimeUnit.MINUTES).pollInterval(Duration.ofSeconds(1)).untilAsserted(() -> {
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
    public void kafkaConsumerShouldTrace() throws InterruptedException {
        sendAndReceiveEvents(1, 1);
        final int pageLimit = 50;
        final String expectedOperationName = "stock-price receive";
        await().atMost(1, TimeUnit.MINUTES).pollInterval(Duration.ofSeconds(1)).untilAsserted(() -> {
            thenRetrieveTraces(pageLimit, "1h", getServiceName(), expectedOperationName);
            thenStatusCodeMustBe(HttpStatus.SC_OK);
            thenTraceDataSizeMustBe(greaterThan(0));
            thenTraceSpanSizeMustBe(greaterThan(0));
            thenTraceSpanTagsSizeMustBe(greaterThan(0));
            thenTraceSpansOperationNameMustBe(not(empty()));
            thenCheckThatAllOperationNamesAreEqualTo(expectedOperationName);
        });
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

    private String getServiceName() {
        // TODO https://github.com/quarkusio/quarkus/issues/16499
        return (IS_NATIVE) ? "301-quarkus-vertx-kafka" : "<<unset>>";
    }

    private void sendAndReceiveEvents(int timeoutMin, int expectedEventsAmount) throws InterruptedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getServerSentEventURL());

        final CountDownLatch latch = new CountDownLatch(expectedEventsAmount);

        SseEventSource source = SseEventSource.target(target).build();
        source.register(inboundSseEvent -> latch.countDown());
        source.open();
        boolean completed = latch.await(timeoutMin, TimeUnit.MINUTES);
        assertEquals(true, completed);
        source.close();
    }

    protected abstract String getServerSentEventURL();
}
