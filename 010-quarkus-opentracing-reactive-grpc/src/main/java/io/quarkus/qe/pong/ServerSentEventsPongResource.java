package io.quarkus.qe.pong;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.qe.traceable.TraceableResource;
import io.smallrye.mutiny.Multi;

@Path("/server-sent-events-pong")
public class ServerSentEventsPongResource extends TraceableResource {

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> getPong() {
        recordTraceId();
        return Multi.createFrom().item("pong");
    }
}