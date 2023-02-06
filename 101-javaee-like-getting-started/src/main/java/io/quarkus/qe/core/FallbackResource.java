package io.quarkus.qe.core;

import org.eclipse.microprofile.faulttolerance.Fallback;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/fallback")
public class FallbackResource {

    public static final String WORKED = "WORKED";
    public static final String FAILED = "FAILED";

    @GET
    @Path("/work")
    @Fallback(fallbackMethod = "failed")
    public String work() {
        return WORKED;
    }

    @GET
    @Path("/fail")
    @Fallback(fallbackMethod = "failed")
    public String fail() {
        throw new RuntimeException("Boom!");
    }

    String failed() {
        return FAILED;
    }
}
