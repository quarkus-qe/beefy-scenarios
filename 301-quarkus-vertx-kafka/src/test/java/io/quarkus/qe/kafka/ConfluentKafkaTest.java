package io.quarkus.qe.kafka;

import io.quarkus.qe.kafka.resources.ConfluentTestProfile;
import io.quarkus.qe.kafka.resources.JaegerTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(ConfluentTestProfile.class)
@QuarkusTestResource(JaegerTestResource.class)
public class ConfluentKafkaTest extends KafkaCommonTest {
    private static final String STOCK_MONITOR_SSE_ENDPOINT = "http://localhost:8081/stock/stream";

    @Override
    protected String getServerSentEventURL() {
        return STOCK_MONITOR_SSE_ENDPOINT;
    }
}
