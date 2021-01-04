package io.quarkus.qe.spring.data;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("/session")
public class SessionResource {
    @PUT
    @Path("/invalidate")
    public void invalidate(final @Context HttpServletRequest req) {
        req.getSession().invalidate();
    }
}
