package io.quarkus.qe.ping;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.qe.model.Score;
import io.quarkus.qe.ping.clients.ReactivePongClient;
import io.smallrye.mutiny.Uni;

@Path("/reactive-ping")
public class ReactivePingResource {

    @Inject
    @RestClient
    ReactivePongClient pongClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> getPing() {
        return pongClient.getPong().onItem().transform(response -> "ping " + response);
    }

    @GET
    @Path("/name/{name}")
    public Uni<String> getPongWithPathName(@PathParam("name") String name) {
        return pongClient.getPongWithPathName(name).onItem().transform(response -> "ping " + response);
    }

    @POST
    @Path("/withBody")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> createPongWithBody(Score score) {
        return pongClient.createPongWithBody(score).onItem().transform(response -> "ping -> " + response);
    }

    @PUT
    @Path("/withBody")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> updatePongWithBody(Score score) {
        return pongClient.updatePongWithBody(score).onItem().transform(response -> "ping -> " + response);
    }

    @DELETE
    @Path("/{id}")
    public Uni<String> deleteById(@PathParam("id") String id) {
        return pongClient.deletePongById(id).onItem().transform(response -> "ping -> " + response);
    }
}
