package io.quarkus.qe.pong;

import io.quarkus.qe.traceable.TraceableResource;
import io.smallrye.mutiny.Uni;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/reactive-pong")
public class ReactivePongResource extends TraceableResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> getPong() {
        recordTraceId();
        return Uni.createFrom().item("pong");
    }
}
