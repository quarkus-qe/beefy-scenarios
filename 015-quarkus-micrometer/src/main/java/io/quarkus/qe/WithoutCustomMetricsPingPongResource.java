package io.quarkus.qe;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/without-metrics-pingpong")
public class WithoutCustomMetricsPingPongResource {

    private static final String PING_PONG = "ping pong";

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String simpleScenario() {
        return PING_PONG;
    }
}