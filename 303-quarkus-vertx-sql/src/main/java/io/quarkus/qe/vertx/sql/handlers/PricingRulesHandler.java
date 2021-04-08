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

import io.quarkus.qe.vertx.sql.domain.PricingRules;
import io.quarkus.qe.vertx.sql.domain.Record;
import io.quarkus.qe.vertx.sql.services.DbPoolService;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.Route.HttpMethod;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.ext.web.RoutingContext;

@Tag(name = "Pricing Rules", description = "Manage your business pricing rules")
@Singleton
@RouteBase(path = "pricingRules", produces = "application/json")
public class PricingRulesHandler {

    @Inject
    @Named("sqlClient")
    DbPoolService connection;

    @Operation(summary = "Retrieve all pricing rules")
    @APIResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, implementation = PricingRules.class)))
    @Route(methods = HttpMethod.GET, path = "/*")
    void allPricingRules(RoutingContext context) {
        PricingRules.findAllAsList(connection)
                .onFailure().invoke(context::fail)
                .subscribe().with(pricingRules -> context.response().end(Record.toJsonStringify(pricingRules)));
    }
}
