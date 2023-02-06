package io.quarkus.qe.pong;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.quarkus.qe.model.Score;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Tag(name = "Pong", description = "Pong API")
@Path("/rest-pong")
public class PongResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPong() {
        return "pong";
    }

    @GET
    @Path("/name/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPongWithName(@PathParam("name") String name) {
        return "pong " + name;
    }

    @POST
    @Path("/withBody")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createPongWithBody(Score score) {
        return score.toString();
    }

    @PUT
    @Path("/withBody")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String updatePongWithBody(Score score) {
        return score.toString();
    }

    @DELETE
    @Path("/{id}")
    public boolean deleteById(@PathParam("id") String id) {
        return true;
    }
}
