package io.quarkus.qe.ping.clients;

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

import io.quarkus.qe.model.Score;
import io.smallrye.mutiny.Uni;

@RegisterRestClient
@RegisterClientHeaders
@Path("/reactive-pong")
public interface ReactivePongClient {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    Uni<String> getPong();

    @GET
    @Path("/name/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    Uni<String> getPongWithPathName(@PathParam("name") String name);

    @POST
    @Path("/withBody")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    Uni<String> createPongWithBody(Score score);

    @PUT
    @Path("/withBody")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    Uni<String> updatePongWithBody(Score score);

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    Uni<Boolean> deletePongById(@PathParam("id") String id);
}
