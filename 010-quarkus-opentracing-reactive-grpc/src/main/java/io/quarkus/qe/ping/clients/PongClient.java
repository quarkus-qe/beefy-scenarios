package io.quarkus.qe.ping.clients;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
public interface PongClient {
    @GET
    @Path("/rest-pong")
    @Produces(MediaType.TEXT_PLAIN)
    String getPong();

}
