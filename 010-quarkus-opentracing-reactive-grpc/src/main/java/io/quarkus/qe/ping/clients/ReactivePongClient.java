package io.quarkus.qe.ping.clients;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient
public interface ReactivePongClient {
    @GET
    @Path("/reactive-pong")
    @Produces(MediaType.TEXT_PLAIN)
    Uni<String> getPong();

}
