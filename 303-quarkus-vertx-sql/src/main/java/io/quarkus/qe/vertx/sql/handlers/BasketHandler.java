package io.quarkus.qe.vertx.sql.handlers;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.quarkus.qe.vertx.sql.domain.Basket;
import io.quarkus.qe.vertx.sql.domain.Record;
import io.quarkus.qe.vertx.sql.services.DbPoolService;
import io.quarkus.vertx.web.Body;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.Route.HttpMethod;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.ext.web.RoutingContext;

@Tag(name = "Basket", description = "Manage your basket")
@Singleton
@RouteBase(path = "basket", produces = "application/json")
public class BasketHandler {

    @Inject
    @Named("sqlClient")
    DbPoolService connection;

    @Operation(summary = "save basket")
    @APIResponse(responseCode = "201", description = "basket id", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.OBJECT, implementation = Record.class)))
    @Route(methods = HttpMethod.POST, path = "/checkout")
    void search(@Body Basket basket, RoutingContext context) {
        basket.save(connection)
                .onFailure().invoke(context::fail)
                .subscribe().with(id -> context.response().setStatusCode(201).end(new Record(id).toJsonStringify()));
    }

}
