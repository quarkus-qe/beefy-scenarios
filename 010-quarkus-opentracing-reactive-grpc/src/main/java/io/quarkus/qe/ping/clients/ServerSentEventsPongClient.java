package io.quarkus.qe.ping.clients;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.reactivestreams.Publisher;

@RegisterRestClient
public interface ServerSentEventsPongClient {
    @GET
    @Path("/server-sent-events-pong")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    Publisher<String> getPong();

}
