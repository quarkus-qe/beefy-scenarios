package io.quarkus.qe.vertx.web.auth;

import org.jboss.logging.Logger;

import io.quarkus.security.UnauthorizedException;
import io.vertx.core.json.Json;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuthZ {

    private static final Logger LOG = Logger.getLogger(AuthZ.class);

    public void authorize(final RoutingContext context) {
        User user = context.user();
        LOG.info("Ready for AuthZ. User: " + Json.encode(user));
        if (!user.principal().getJsonArray("groups").contains("admin")) {
            throw new UnauthorizedException("Unexpected user group.");
        }

        context.next();
    }
}
