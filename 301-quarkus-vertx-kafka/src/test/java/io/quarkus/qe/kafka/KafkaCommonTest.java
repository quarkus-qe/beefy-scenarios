package io.quarkus.qe.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;

import org.junit.jupiter.api.Test;

abstract class KafkaCommonTest {

    @Test
    public void producerConsumesTest() throws InterruptedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getServerSentEventURL());

        final CountDownLatch latch = new CountDownLatch(1);

        SseEventSource source = SseEventSource.target(target).build();
        source.register(inboundSseEvent -> latch.countDown());
        source.open();
        boolean completed = latch.await(1, TimeUnit.MINUTES);
        assertEquals(true, completed);
        source.close();
    }

    protected abstract String getServerSentEventURL();
}
