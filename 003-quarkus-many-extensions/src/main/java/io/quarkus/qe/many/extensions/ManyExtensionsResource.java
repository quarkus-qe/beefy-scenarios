package io.quarkus.qe.many.extensions;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/hello")
public class ManyExtensionsResource {

    @GET
    @Path("/")
    public String hello() {
        return "hello";
    }
}
