package io.quarkus.qe.ping.clients;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;

@RegisterRestClient
public interface ReactivePongClient {
    @GET
    @Path("/reactive-pong")
    @Produces(MediaType.TEXT_PLAIN)
    Uni<String> getPong();

}
