package io.quarkus.qe.kafka;

import io.quarkus.qe.kafka.resources.StrimziKafkaResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@QuarkusTestResource(StrimziKafkaResource.class)
public class AlertMonitorTest {

    private static final String ALERT_MONITOR_SSE_ENDPOINT = "http://localhost:8081/monitor/stream";

    @Test
    void testAlertMonitorEventStream() throws InterruptedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(ALERT_MONITOR_SSE_ENDPOINT);

        final CountDownLatch latch = new CountDownLatch(3);

        SseEventSource source = SseEventSource.target(target).build();
        source.register(inboundSseEvent -> latch.countDown());
        source.open();
        boolean completed = latch.await(15, TimeUnit.SECONDS);
        assertEquals(true, completed);
        source.close();
    }
}
