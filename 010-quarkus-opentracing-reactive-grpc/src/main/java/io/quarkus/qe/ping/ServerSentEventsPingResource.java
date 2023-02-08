package io.quarkus.qe.ping;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.qe.ping.clients.ServerSentEventsPongClient;
import io.quarkus.qe.traceable.TraceableResource;
import io.smallrye.mutiny.Multi;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/server-sent-events-ping")
public class ServerSentEventsPingResource extends TraceableResource {

    @Inject
    @RestClient
    ServerSentEventsPongClient pongClient;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> getPing() {
        recordTraceId();
        return Multi.createFrom().publisher(pongClient.getPong()).map(response -> "ping " + response);
    }
}