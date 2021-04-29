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

@Schema(name = "Airport", description = "Airport entity")
@RegisterForReflection
public class Airport extends Record {

    private static final String QUALIFIED_CODE_NAME = "iata_code";
    @Schema(description = "IATA code")
    private String code;

    private static final String QUALIFIED_CITY_NAME = "city";
    @Schema(description = "City name")
    private String city;

    public Airport(long id, String code, String city) {
        super.setId(id);
        this.code = code;
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private static Airport from(Row row) {
        return new Airport(row.getLong(QUALIFIED_ID), row.getString(QUALIFIED_CODE_NAME), row.getString(QUALIFIED_CITY_NAME));
    }

    protected static Multi<Airport> fromSet(RowSet<Row> rows) {
        return Multi.createFrom().iterable(rows).onItem().transform(Airport::from);
    }

    public static Multi<Airport> findAll(DbPoolService client) {
        return client.query("SELECT * FROM " + client.getDatabaseName() + ".airports").execute().onItem()
                .transformToMulti(Airport::fromSet);
    }

    public static Uni<List<Airport>> findAllAsList(DbPoolService client) {
        return findAll(client).collect().in(ArrayList::new, List::add);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Airport airport = (Airport) o;
        return code.equals(airport.code) &&
                city.equals(airport.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, city);
    }
}
