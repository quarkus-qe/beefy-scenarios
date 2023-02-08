package io.quarkus.qe.ping.clients;

import java.util.concurrent.Flow.Publisher;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient
public interface ServerSentEventsPongClient {
    @GET
    @Path("/server-sent-events-pong")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    Publisher<String> getPong();

}
