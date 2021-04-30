package io.quarkus.qe.pong;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.qe.model.Score;
import io.smallrye.mutiny.Uni;

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
