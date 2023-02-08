package io.quarkus.qe.spring.data;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;

@Path("/session")
public class SessionResource {
    @PUT
    @Path("/invalidate")
    public void invalidate(final @Context HttpServletRequest req) {
        req.getSession().invalidate();
    }
}
