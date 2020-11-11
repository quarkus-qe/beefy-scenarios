package io.quarkus.qe.prices;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;

/**
 * A simple resource retrieving the in-memory "my-data-stream" and sending the items as server-sent events.
 */
@ApplicationScoped
public class PriceConsumer {

    private final ConcurrentLinkedQueue<Double> prices = new ConcurrentLinkedQueue<>();

    @Incoming("my-data-stream")
    public void process(Double price) {
        this.prices.add(price);
    }

    public Queue<Double> getPrices() {
        return prices;
    }
}