package io.quarkus.qe.vertx.sql.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.qe.vertx.sql.services.DbPoolService;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlClientHelper;

@Schema(name = "Basket", description = "Basket entity")
@RegisterForReflection
public class Basket extends Record {

    private String flight;
    private double price;
    private Passenger billingPassenger;

    public Basket(String flight, double price) {
        this.flight = flight;
        this.price = price;
    }

    public Basket() {
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Passenger getBillingPassenger() {
        return billingPassenger;
    }

    public void setBillingPassenger(Passenger billingPassenger) {
        this.billingPassenger = billingPassenger;
    }

    public Uni<Long> save(DbPoolService sqlClient) {
        return SqlClientHelper.inTransactionUni(sqlClient,
                tx -> billingPassenger.save(sqlClient).onItem().transformToUni(passenger_id -> {
                    List<String> fieldsNames = Arrays.asList("flight,price,created_at,passenger_id".split(","));
                    List<Object> fieldsValues = Stream.of(getFlight(), getPrice(), getCreatedAt(), passenger_id)
                            .collect(Collectors.toList());
                    return sqlClient.save("basket", fieldsNames, fieldsValues);
                }));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Basket basket = (Basket) o;
        return Double.compare(basket.price, price) == 0 &&
                flight.equals(basket.flight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flight, price);
    }
}
