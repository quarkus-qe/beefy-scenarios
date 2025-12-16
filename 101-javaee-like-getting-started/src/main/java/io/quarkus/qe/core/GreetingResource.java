package io.quarkus.qe.core;

import io.micrometer.core.annotation.Counted;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Counted(value = "hello_invocation_count")
    public String hello() {
        return "hello";
    }
}
