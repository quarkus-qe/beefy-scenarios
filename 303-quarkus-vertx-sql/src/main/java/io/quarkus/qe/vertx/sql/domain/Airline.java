package io.quarkus.qe.vertx.sql.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.qe.vertx.sql.services.DbPoolService;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;

@Schema(name = "Airline", description = "Airline entity")
@RegisterForReflection
public class Airline extends Record {

    private static final String QUALIFIED_CODE_NAME = "iata_code";
    @Schema(description = "IATA code")
    private String code;

    private static final String QUALIFIED_NAME_NAME = "name";
    @Schema(description = "Airline company name")
    private String name;

    private static final String QUALIFIED_INFANT_PRICE_NAME = "infant_price";
    @Schema(description = "Child price")
    private double infantPrice;

    public Airline(long id, String code, String name, double infantPrice) {
        super.setId(id);
        this.code = code;
        this.name = name;
        this.infantPrice = infantPrice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getInfantPrice() {
        return infantPrice;
    }

    public void setInfantPrice(double infantPrice) {
        this.infantPrice = infantPrice;
    }

    private static Airline from(Row row) {
        return new Airline(row.getLong(QUALIFIED_ID), row.getString(QUALIFIED_CODE_NAME), row.getString(QUALIFIED_NAME_NAME),
                row.getFloat(QUALIFIED_INFANT_PRICE_NAME));
    }

    protected static Multi<Airline> fromSet(RowSet<Row> rows) {
        return Multi.createFrom().iterable(rows).onItem().transform(Airline::from);
    }

    public static Multi<Airline> findAll(DbPoolService client) {
        return client.query("SELECT * FROM " + client.getDatabaseName() + ".airlines").execute().onItem()
                .transformToMulti(Airline::fromSet);
    }

    public static Uni<List<Airline>> findAllAsList(DbPoolService client) {
        return findAll(client).collect().in(ArrayList::new, List::add);
    }

    public static Uni<Map<String, Airline>> findAirlineAsMap(DbPoolService client) {
        return findAll(client).collect().asMap(airline -> airline.code);
    }

    public static String codeFromFlight(String flightCode) {
        return flightCode.length() < 2 ? flightCode.toUpperCase() : flightCode.substring(0, 2).toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Airline airline = (Airline) o;
        return Double.compare(airline.infantPrice, infantPrice) == 0 &&
                code.equals(airline.code) &&
                name.equals(airline.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, infantPrice);
    }
}
