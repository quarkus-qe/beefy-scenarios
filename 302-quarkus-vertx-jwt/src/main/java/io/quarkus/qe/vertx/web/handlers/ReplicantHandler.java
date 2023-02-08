package io.quarkus.qe.vertx.web.handlers;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;

import io.quarkus.qe.vertx.web.model.Replicant;
import io.quarkus.qe.vertx.web.services.ReplicantService;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ReplicantHandler {

    @Inject
    ReplicantService replicantService;

    public void upsertReplicant(final RoutingContext context) {
        Replicant replicant = context.getBodyAsJson().mapTo(Replicant.class);
        replicantService.upsert(replicant)
                .onFailure().invoke(context::fail)
                .subscribe().with(success -> context.response()
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("id", replicant.getId()).encode()));
    }

    public void deleteReplicant(final RoutingContext context) {
        String id = context.request().getParam("id");
        replicantService.delete(id).onFailure().invoke(context::fail)
                .subscribe().with(success -> context.response()
                        .putHeader("Content-Type", "application/json")
                        .setStatusCode(NO_CONTENT.code()).end());
    }

    public void getReplicantById(final RoutingContext context) {
        String id = context.request().getParam("id");
        replicantService.get(id).onFailure().invoke(context::fail)
                .subscribe().with(replicant -> context.response()
                        .putHeader("Content-Type", "application/json")
                        .end(replicant.toJsonEncoded()));
    }

    public void getAllReplicant(final RoutingContext context) {
        replicantService.get().onFailure().invoke(context::fail)
                .subscribe().with(replicants -> context.response()
                        .putHeader("Content-Type", "application/json")
                        .end(Json.encode(replicants)));
    }
}
