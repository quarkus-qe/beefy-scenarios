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

import io.quarkus.qe.vertx.sql.domain.Airport;
import io.quarkus.qe.vertx.sql.domain.Record;
import io.quarkus.qe.vertx.sql.services.DbPoolService;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.Route.HttpMethod;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.ext.web.RoutingContext;

@Tag(name = "Airports", description = "Manage your airports")
@Singleton
@RouteBase(path = "airports", produces = "application/json")
public class AirportsHandler {

    @Inject
    @Named("sqlClient")
    DbPoolService connection;

    @Operation(summary = "Retrieve all flights")
    @APIResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, implementation = Airport.class)))
    @Route(methods = HttpMethod.GET, path = "/*")
    void allAirports(RoutingContext context) {
        Airport.findAllAsList(connection)
                .onFailure().invoke(context::fail)
                .subscribe().with(airports -> context.response().end(Record.toJsonStringify(airports)));
    }
}
