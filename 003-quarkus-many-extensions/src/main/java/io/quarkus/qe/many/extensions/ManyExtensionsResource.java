package io.quarkus.qe.many.extensions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello")
public class ManyExtensionsResource {

    @GET
    @Path("/")
    public String hello() {
        return "hello";
    }
}
