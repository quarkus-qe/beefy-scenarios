package io.quarkus.qe.ping;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.qe.ping.clients.ReactivePongClient;
import io.smallrye.mutiny.Uni;

@Path("/reactive-ping")
public class ReactivePingResource {

    @Inject
    @RestClient
    ReactivePongClient pongClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> getPing() {
        return pongClient.getPong().onItem().transform(response -> {
            return "ping " + response;
        });
    }
}