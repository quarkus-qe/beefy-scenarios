package io.quarkus.qe.kafka;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AlertMonitorTest extends KafkaCommonTest {

    private static final String ALERT_MONITOR_SSE_ENDPOINT = "http://localhost:8083/monitor/stream";

    @Override
    protected String getServerSentEventURL() {
        return ALERT_MONITOR_SSE_ENDPOINT;
    }
}
