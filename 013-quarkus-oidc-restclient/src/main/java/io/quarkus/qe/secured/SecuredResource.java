package io.quarkus.qe.secured;

import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/secured")
@DenyAll
@RequestScoped
public class SecuredResource {
    @Inject
    JsonWebToken jwt;

    @Inject
    @Claim(standard = Claims.iss)
    String issuer;

    @Inject
    @Claim(standard = Claims.groups)
    Set<String> groups;

    @GET
    @Path("/getClaimsFromBeans")
    @RolesAllowed("**")
    public String getClaimsFromBeans(@Context SecurityContext security) {
        return "Hello, " + jwt.getName() + ", your token was issued by " + issuer + " and you are in groups " + groups;
    }

    @GET
    @Path("/getClaimsFromToken")
    @RolesAllowed("**")
    public String getClaimsFromToken(@Context SecurityContext security) {
        return "Hello, " + jwt.getName() + ", your token was issued by " + jwt.getClaim(Claims.iss) + " and you are in groups "
                + jwt.getClaim(Claims.groups);
    }

    @GET
    @Path("/admin")
    @RolesAllowed("admin")
    public String admin() {
        return "Restricted area! Admin access granted!";
    }
}
