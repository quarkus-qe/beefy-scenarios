package io.quarkus.qe.vertx.web;

import static io.quarkus.qe.vertx.web.Application.AUTH.NO_SECURE;
import static io.quarkus.qe.vertx.web.Application.AUTH.SECURE;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import io.quarkus.qe.vertx.web.auth.AuthZ;
import io.quarkus.qe.vertx.web.exceptions.FailureHandler;
import io.quarkus.qe.vertx.web.handlers.BladeRunnerHandler;
import io.quarkus.qe.vertx.web.handlers.JWTHandler;
import io.quarkus.qe.vertx.web.handlers.ReplicantHandler;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.LoggerHandler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class Application {

    private static final Logger LOG = Logger.getLogger(Application.class);

    @ConfigProperty(name = "app.name")
    public String serviceName;

    @Inject
    ReplicantHandler replicant;

    @Inject
    BladeRunnerHandler bladeRunner;

    @Inject
    FailureHandler failureHandler;

    @Inject
    JWTHandler jwt;

    @Inject
    JWTAuth authN;

    @Inject
    AuthZ authZ;

    Router router;

    enum AUTH {
        SECURE,
        NO_SECURE
    }

    void init(@Observes Router router) {
        this.router = router;
    }

    void onStart(@Observes StartupEvent ev) {
        LOG.info(String.format("Application %s starting...", serviceName));

        addRoute(HttpMethod.POST, "/bladeRunner", SECURE, rc -> bladeRunner.upsertBladeRunner(rc));
        addRoute(HttpMethod.GET, "/bladeRunner/:id", SECURE, rc -> bladeRunner.getBladeRunnerById(rc));
        addRoute(HttpMethod.GET, "/bladeRunner", SECURE, rc -> bladeRunner.getAllBladeRunner(rc));
        addRoute(HttpMethod.DELETE, "/bladeRunner/:id", SECURE, rc -> bladeRunner.deleteBladeRunner(rc));

        addRoute(HttpMethod.POST, "/replicant", SECURE, rc -> replicant.upsertReplicant(rc));
        addRoute(HttpMethod.GET, "/replicant/:id", NO_SECURE, rc -> replicant.getReplicantById(rc));
        addRoute(HttpMethod.GET, "/replicant", SECURE, rc -> replicant.getAllReplicant(rc));
        addRoute(HttpMethod.DELETE, "/replicant/:id", SECURE, rc -> replicant.deleteReplicant(rc));

        addRoute(HttpMethod.GET, "/jwt", NO_SECURE, rc -> jwt.createJwt(rc));
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOG.info(String.format("Application %s stopping...", serviceName));
    }

    private void addRoute(HttpMethod method, String path, AUTH authEnabled, Handler<RoutingContext> handler) {
        Route route = this.router.route(method, path)
                .handler(LoggerHandler.create())
                .handler(CorsHandler.create("*"));

        if (method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT))
            route.handler(BodyHandler.create());

        if (authEnabled == SECURE)
            route.handler(JWTAuthHandler.create(authN)).handler(authZ::authorize);

        route.handler(handler).failureHandler(rc -> failureHandler.handler(rc));
    }
}
