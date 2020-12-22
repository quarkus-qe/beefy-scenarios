package io.quarkus.qe.prices;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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