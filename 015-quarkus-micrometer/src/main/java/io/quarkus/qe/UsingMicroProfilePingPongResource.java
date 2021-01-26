package io.quarkus.qe;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.Counted;

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