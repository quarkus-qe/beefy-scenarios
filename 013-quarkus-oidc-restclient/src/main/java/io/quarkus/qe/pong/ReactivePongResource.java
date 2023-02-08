package io.quarkus.qe.pong;

import io.quarkus.qe.model.Score;
import io.smallrye.mutiny.Uni;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/reactive-pong")
public class ReactivePongResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> getPong() {
        return Uni.createFrom().item("pong");
    }

    @GET
    @Path("/name/{name}")
    public Uni<String> getPongWithName(@PathParam("name") String name) {
        return Uni.createFrom().item("pong " + name);
    }

    @POST
    @Path("/withBody")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<String> createPongWithBody(Score score) {
        return Uni.createFrom().item(score.toString());
    }

    @PUT
    @Path("/withBody")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<String> updatePongWithBody(Score score) {
        return Uni.createFrom().item(score.toString());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Boolean> deleteById(@PathParam("id") String id) {
        return Uni.createFrom().item(true);
    }
}
