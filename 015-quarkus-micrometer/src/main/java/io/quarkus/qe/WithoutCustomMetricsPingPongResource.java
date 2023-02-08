package io.quarkus.qe;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/without-metrics-pingpong")
public class WithoutCustomMetricsPingPongResource {

    private static final String PING_PONG = "ping pong";

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String simpleScenario() {
        return PING_PONG;
    }
}