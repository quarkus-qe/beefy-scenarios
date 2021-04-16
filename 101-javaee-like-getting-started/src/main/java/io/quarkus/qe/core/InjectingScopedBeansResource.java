package io.quarkus.qe.core;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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