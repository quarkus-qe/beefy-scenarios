package io.quarkus.qe;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;

import org.junit.jupiter.api.Test;

public abstract class BaseAlertMonitorTest {

    private static final String ALERT_MONITOR_SSE_ENDPOINT = "http://localhost:8081/monitor/stream";

    @Test
    void testAlertMonitorEventStream() throws InterruptedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(ALERT_MONITOR_SSE_ENDPOINT);

        final CountDownLatch latch = new CountDownLatch(1);

        SseEventSource source = SseEventSource.target(target).build();
        source.register(inboundSseEvent -> latch.countDown());
        source.open();
        boolean completed = latch.await(5, TimeUnit.MINUTES);
        assertTrue(completed);
        source.close();
    }
}
