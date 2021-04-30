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

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.quarkus.qe.model.Score;

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
