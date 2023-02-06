package io.quarkus.qe.prices;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * A simple resource retrieving the in-memory "my-data-stream" and sending the items as server-sent events.
 */
@Path("/prices")
public class PriceResource {

    @Inject
    PriceConsumer repository;

    @GET
    @Path("/poll")
    @Produces(MediaType.TEXT_PLAIN)
    public Double get() {
        return repository.getPrices().poll();
    }
}