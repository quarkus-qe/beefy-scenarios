package io.quarkus.qe;

import java.util.stream.IntStream;

import io.micrometer.core.instrument.MeterRegistry;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

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
