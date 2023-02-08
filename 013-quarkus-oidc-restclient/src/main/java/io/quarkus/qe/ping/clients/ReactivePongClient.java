package io.quarkus.qe.ping.clients;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

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
