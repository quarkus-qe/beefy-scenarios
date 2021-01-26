package io.quarkus.qe.ping.clients;

import io.quarkus.qe.model.Score;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
@RegisterClientHeaders
@Path("/rest-pong")
public interface PongClient {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String getPong();

    @GET
    @Path("/name/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    String getPongWithPathName(@PathParam("name") String name);

    @POST
    @Path("/withBody")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    String createPongWithBody(Score score);

    @PUT
    @Path("/withBody")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    String updatePongWithBody(Score score);

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    boolean deletePongById(@PathParam("id") String id);
}
