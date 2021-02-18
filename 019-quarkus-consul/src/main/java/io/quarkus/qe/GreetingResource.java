package io.quarkus.qe;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/api")
public class GreetingResource {

    @ConfigProperty(name = "my.property")
    String property;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello " + property;
    }
}