package io.quarkus.qe.pong;

import io.quarkus.qe.traceable.TraceableResource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/rest-pong")
public class PongResource extends TraceableResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPong() {
        recordTraceId();
        return "pong";
    }
}
