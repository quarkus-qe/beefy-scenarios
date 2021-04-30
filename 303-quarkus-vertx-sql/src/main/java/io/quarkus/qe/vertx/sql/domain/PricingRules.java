package io.quarkus.qe.vertx.sql.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.qe.vertx.sql.services.DbPoolService;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;

@Schema(name = "PricingRules", description = "PricingRules entity")
@RegisterForReflection
public class PricingRules extends Record {
    private static final String QUALIFIED_FROM_NAME = "days_to_departure";
    @Schema(description = "days_to_departure")
    private int from;

    private static final String QUALIFIED_TO_NAME = "until";
    @Schema(description = "until")
    private int to;

    private static final String QUALIFIED_PERCENTAGE_NAME = "percentage";
    @Schema(description = "% of the base price")
    private int percentage;

    private static final int CHILD_PERCENTAGE = 67;

    public PricingRules(long id, int from, int to, int percentage) {
        this.setId(id);
        this.from = from;
        this.to = to;
        this.percentage = percentage;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    protected static PricingRules from(Row row) {
        return new PricingRules(row.getLong(QUALIFIED_ID), row.getInteger(QUALIFIED_FROM_NAME),
                row.getInteger(QUALIFIED_TO_NAME), row.getInteger(QUALIFIED_PERCENTAGE_NAME));
    }

    protected static Multi<PricingRules> fromSet(RowSet<Row> rows) {
        return Multi.createFrom().iterable(rows).onItem().transform(PricingRules::from);
    }

    public static Multi<PricingRules> findAll(DbPoolService client) {
        return client.query("SELECT * FROM " + client.getDatabaseName() + ".pricingRules").execute().onItem()
                .transformToMulti(PricingRules::fromSet);
    }

    public static Uni<List<PricingRules>> findAllAsList(DbPoolService client) {
        return findAll(client).collect().in(ArrayList::new, List::add);
    }

    public static double applyChildPercentage(double adultFinalPrice) {
        return (adultFinalPrice * CHILD_PERCENTAGE) / 100;
    }

    public static Predicate<PricingRules> daysToDepartureFilter(int dayToDeparture) {
        return pricingRule -> pricingRule.getFrom() <= dayToDeparture + 1 && pricingRule.getTo() >= dayToDeparture;
    }

    public double applyAdultPercentage(double basePrice) {
        return (basePrice * percentage) / 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PricingRules that = (PricingRules) o;
        return from == that.from &&
                to == that.to &&
                percentage == that.percentage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, percentage);
    }
}
