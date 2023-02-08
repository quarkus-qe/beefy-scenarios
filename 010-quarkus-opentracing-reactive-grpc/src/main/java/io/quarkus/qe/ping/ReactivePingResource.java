package io.quarkus.qe.ping;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.qe.ping.clients.ReactivePongClient;
import io.quarkus.qe.traceable.TraceableResource;
import io.smallrye.mutiny.Uni;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/reactive-ping")
public class ReactivePingResource extends TraceableResource {

    @Inject
    @RestClient
    ReactivePongClient pongClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> getPing() {
        recordTraceId();
        return pongClient.getPong().onItem().transform(response -> {
            return "ping " + response;
        });
    }
}
