package io.quarkus.qe.ping.clients;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
// TODO: Can't use both OIDC Client Filter and OIDC Token extensions. Reported in https://github.com/quarkusio/quarkus/issues/14466
//@AccessToken
public interface TokenPropagationPongClient {

    @GET
    @Path("/rest-pong")
    @Produces(MediaType.TEXT_PLAIN)
    String getPong();

}
