package io.quarkus.qe;

import org.eclipse.microprofile.metrics.annotation.Counted;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/using-microprofile-pingpong")
public class UsingMicroProfilePingPongResource {

    private static final String PING_PONG = "ping pong";

    @GET
    @Counted(name = "simple_mp", absolute = true)
    @Produces(MediaType.TEXT_PLAIN)
    public String simpleScenario() {
        return PING_PONG;
    }
}
