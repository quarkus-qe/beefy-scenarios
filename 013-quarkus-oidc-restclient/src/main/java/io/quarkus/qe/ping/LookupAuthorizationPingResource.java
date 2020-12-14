package io.quarkus.qe.ping;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.qe.ping.clients.LookupAuthorizationPongClient;

@Path("/rest-lookup-auth-ping")
public class LookupAuthorizationPingResource {

    @Inject
    @RestClient
    LookupAuthorizationPongClient pongClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPing() {
        return "ping " + pongClient.getPong();
    }
}