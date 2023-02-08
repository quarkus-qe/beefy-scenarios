package io.quarkus.qe.kafka.rest;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/stock")
public class StockPriceMonitor {

    @Inject
    @Channel("stock-monitor")
    Publisher<String> stockMarket;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType("application/json")
    public Publisher<String> stream() {
        return stockMarket;
    }
}
