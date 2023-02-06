package io.quarkus.qe.ping.clients;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient
public interface PongClient {
    @GET
    @Path("/rest-pong")
    @Produces(MediaType.TEXT_PLAIN)
    String getPong();

}
