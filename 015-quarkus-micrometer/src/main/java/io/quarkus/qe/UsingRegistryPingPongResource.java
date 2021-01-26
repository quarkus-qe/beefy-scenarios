package io.quarkus.qe;

import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.micrometer.core.instrument.MeterRegistry;

@Path("/using-registry-pingpong")
public class UsingRegistryPingPongResource {

    private static final String PING_PONG = "ping pong";

    @Inject
    MeterRegistry registry;

    @GET
    @Path("/simple_registry")
    @Produces(MediaType.TEXT_PLAIN)
    public String simpleScenario() {
        incrementCounter("simple_registry");

        return PING_PONG;
    }

    @GET
    @Path("/forloop")
    @Produces(MediaType.TEXT_PLAIN)
    public String iterativeScenario(@QueryParam("count") int count) {
        IntStream.range(0, count).forEach(i -> incrementCounter("forloop"));

        return PING_PONG;
    }

    @GET
    @Path("/forloopparallel")
    @Produces(MediaType.TEXT_PLAIN)
    public String iterativeParallelScenario(@QueryParam("count") int count) {
        IntStream.range(0, count).parallel().forEach(i -> incrementCounter("forloopparallel"));

        return PING_PONG;
    }

    private void incrementCounter(String name) {
        registry.counter(name).increment();
    }
}