package io.quarkus.qe.kafka;

import org.junit.jupiter.api.Disabled;

import io.quarkus.qe.kafka.resources.JaegerTestResource;
import io.quarkus.qe.kafka.resources.StrimziKafkaResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.NativeImageTest;

@Disabled(value = "Kafka failing when using Apicurio registry on Native: https://github.com/quarkusio/quarkus/issues/17829")
@NativeImageTest
@QuarkusTestResource(StrimziKafkaResource.class)
@QuarkusTestResource(JaegerTestResource.class)
public class StrimziKafkaIT extends KafkaCommonTest {

    private static final String STOCK_MONITOR_SSE_ENDPOINT = "http://localhost:8083/stock/stream";

    @Override
    protected String getServerSentEventURL() {
        return STOCK_MONITOR_SSE_ENDPOINT;
    }
}
