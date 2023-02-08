package io.quarkus.qe.http.non_application.endpoint;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class HelloResource {
    private static final String TEMPLATE = "Hello, %s!";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@QueryParam("name") @DefaultValue("World") String name) {
        return String.format(TEMPLATE, name);
    }
}
