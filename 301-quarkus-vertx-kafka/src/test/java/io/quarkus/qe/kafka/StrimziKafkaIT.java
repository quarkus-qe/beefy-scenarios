package io.quarkus.qe.kafka;

import io.quarkus.qe.kafka.resources.StrimziKafkaResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
@QuarkusTestResource(StrimziKafkaResource.class)
public class StrimziKafkaIT extends KafkaCommonTest{

    private static final String STOCK_MONITOR_SSE_ENDPOINT = "http://localhost:8083/stock/stream";

    @Override
    protected String getServerSentEventURL() {
        return STOCK_MONITOR_SSE_ENDPOINT;
    }
}
