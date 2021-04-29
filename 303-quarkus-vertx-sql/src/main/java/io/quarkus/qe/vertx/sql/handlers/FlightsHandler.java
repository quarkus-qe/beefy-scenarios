package io.quarkus.qe.vertx.sql.handlers;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.Valid;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.quarkus.qe.vertx.sql.domain.Flight;
import io.quarkus.qe.vertx.sql.domain.QueryFlightSearch;
import io.quarkus.qe.vertx.sql.domain.Record;
import io.quarkus.qe.vertx.sql.services.DbPoolService;
import io.quarkus.qe.vertx.sql.services.FlightSearchService;
import io.quarkus.vertx.web.Body;
import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.Route.HttpMethod;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

@OpenAPIDefinition(info = @Info(title = "Flight Search API", version = "1.0.1", contact = @Contact(name = "Flight Search API Support", email = "techsupport@example.com"), license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")))
@Tag(name = "Flights", description = "Manage flights")
@Singleton
@RouteBase(path = "flights", produces = "application/json")
public class FlightsHandler {

    @Inject
    @Named("sqlClient")
    DbPoolService connection;

    @Inject
    FlightSearchService flightSearchService;

    @Operation(summary = "Flight search")
    @APIResponse(responseCode = "200", description = "search flights prices", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.STRING)))
    @Route(methods = HttpMethod.PUT, path = "/search")
    void search(@Body @Valid QueryFlightSearch query, RoutingContext context) {
        flightSearchService.search(query)
                .onFailure().invoke(context::fail)
                .subscribe().with(resp -> context.response().end(Json.encode(resp)));
    }

    @Operation(summary = "Retrieve all flights")
    @APIResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, implementation = Flight.class)))
    @Route(methods = HttpMethod.GET, path = "/*")
    void allFlights(RoutingContext context) {
        Flight.findAllAsList(connection)
                .onFailure().invoke(context::fail)
                .subscribe().with(flights -> context.response().end(Record.toJsonStringify(flights)));
    }

    @Operation(summary = "Retrieve all flights by origin and destination")
    @APIResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, implementation = Flight.class)))
    @Route(methods = HttpMethod.GET, path = "/origin/:origin/destination/:destination")
    void allFlightOriginDest(@Param("origin") String origin, @Param("destination") String destination, RoutingContext context) {
        Flight.findByOriginDestinationAsList(connection, origin, destination)
                .onFailure().invoke(context::fail)
                .subscribe().with(flights -> context.response().end(Record.toJsonStringify(flights)));
    }

}
