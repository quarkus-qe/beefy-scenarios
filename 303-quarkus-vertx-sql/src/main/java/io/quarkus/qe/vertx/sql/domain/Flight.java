package io.quarkus.qe.vertx.sql.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.qe.vertx.sql.services.DbPoolService;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;

@Schema(name = "Flight", description = "Flight entity")
@RegisterForReflection
public class Flight extends Record {

    private static final String QUALIFIED_ORIGIN_NAME = "origin";
    @Schema(description = "origin")
    private String origin;

    private static final String QUALIFIED_DESTINATION_NAME = "destination";
    @Schema(description = "destination")
    private String destination;

    private static final String QUALIFIED_AIRLINE_NAME = "flight_code";
    @Schema(description = "flightCode")
    private String flightCode;

    private static final String QUALIFIED_PRICE_NAME = "base_price";
    @Schema(description = "price")
    private double price;

    public Flight(long id, String origin, String destination, String flightCode, double price) {
        super.setId(id);
        this.origin = origin;
        this.destination = destination;
        this.flightCode = flightCode;
        this.price = price;
    }

    public Flight() {
    }

    protected static Flight from(Row row) {
        return new Flight(row.getLong(QUALIFIED_ID), row.getString(QUALIFIED_ORIGIN_NAME),
                row.getString(QUALIFIED_DESTINATION_NAME), row.getString(QUALIFIED_AIRLINE_NAME),
                row.getDouble(QUALIFIED_PRICE_NAME));
    }

    protected static Multi<Flight> fromSet(RowSet<Row> rows) {
        return Multi.createFrom().iterable(rows).onItem().transform(Flight::from);
    }

    public static Multi<Flight> findAll(DbPoolService client) {
        return client.query("SELECT * FROM " + client.getDatabaseName() + ".flights").execute().onItem()
                .transformToMulti(Flight::fromSet);
    }

    public static Uni<List<Flight>> findAllAsList(DbPoolService client) {
        return findAll(client).collect().in(ArrayList::new, List::add);
    }

    public static Multi<Flight> findByOriginDestination(DbPoolService client, String origin, String destination) {
        String query = String.format("SELECT * FROM " + client.getDatabaseName() + ".flights where %s = '%s' and %s = '%s'",
                QUALIFIED_ORIGIN_NAME, origin, QUALIFIED_DESTINATION_NAME, destination);
        return client.query(query).execute().onItem().transformToMulti(Flight::fromSet);
    }

    public static Uni<List<Flight>> findByOriginDestinationAsList(DbPoolService client, String origin, String destination) {
        return findByOriginDestination(client, origin, destination).collect().in(ArrayList::new, List::add);
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Flight flight = (Flight) o;
        return Double.compare(flight.price, price) == 0 &&
                origin.equals(flight.origin) &&
                destination.equals(flight.destination) &&
                flightCode.equals(flight.flightCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination, flightCode, price);
    }
}
