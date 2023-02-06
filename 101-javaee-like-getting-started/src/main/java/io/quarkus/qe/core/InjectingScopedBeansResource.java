package io.quarkus.qe.core;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/scopedbeans")
public class InjectingScopedBeansResource {

    @Inject
    HttpServletRequest request;

    @Inject
    HttpSession session;

    @Inject
    ServletContext context;

    @GET
    @Path("/sessionId")
    @Produces(MediaType.TEXT_PLAIN)
    public String sessionId() {
        return session.getId();
    }

    @GET
    @Path("/requestId")
    @Produces(MediaType.TEXT_PLAIN)
    public String requestId() {
        return request.getSession().getId();
    }

    @GET
    @Path("/contextPath")
    @Produces(MediaType.TEXT_PLAIN)
    public String contextPath() {
        return context.getContextPath();
    }
}