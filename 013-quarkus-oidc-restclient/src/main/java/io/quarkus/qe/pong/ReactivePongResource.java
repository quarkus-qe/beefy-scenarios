package io.quarkus.qe.pong;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.smallrye.mutiny.Uni;

@Path("/reactive-pong")
public class ReactivePongResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> getPong() {
        return Uni.createFrom().item("pong");
    }
}