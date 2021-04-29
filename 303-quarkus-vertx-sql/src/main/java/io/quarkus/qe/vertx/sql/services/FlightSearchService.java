package io.quarkus.qe.vertx.sql.services;

import static io.quarkus.qe.vertx.sql.domain.Airline.codeFromFlight;
import static io.quarkus.qe.vertx.sql.domain.PricingRules.daysToDepartureFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.quarkus.qe.vertx.sql.domain.Airline;
import io.quarkus.qe.vertx.sql.domain.Basket;
import io.quarkus.qe.vertx.sql.domain.Flight;
import io.quarkus.qe.vertx.sql.domain.PricingRules;
import io.quarkus.qe.vertx.sql.domain.QueryFlightSearch;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;

@Singleton
public class FlightSearchService {

    @Inject
    @Named("sqlClient")
    DbPoolService connection;

    Map<String, Airline> airlines;
    List<PricingRules> pricingRules;

    void onStart(@Observes StartupEvent ev) {
        Airline.findAirlineAsMap(connection).subscribe().with(airlines -> this.airlines = airlines);
        PricingRules.findAllAsList(connection).subscribe().with(pricing -> this.pricingRules = pricing);
    }

    public Uni<List<Basket>> search(QueryFlightSearch query) {
        return Flight.findByOriginDestination(connection, query.from, query.to)
                .onItem()
                .transform(flight -> calculatePrice(query, flight))
                .collect().in(ArrayList::new, List::add);
    }

    private Basket calculatePrice(QueryFlightSearch query, Flight flight) {
        double adultPrice = query.adult * adultPrice(flight, query.daysToDeparture);
        double childPrice = query.child * childPrice(flight, query.daysToDeparture);
        double infantPrice = query.infant * infantPrice(flight);

        return new Basket(flight.getFlightCode(), adultPrice + childPrice + infantPrice);
    }

    private Double infantPrice(Flight flight) {
        return airlines.get(codeFromFlight(flight.getFlightCode())).getInfantPrice();
    }

    private Double childPrice(Flight flight, int dayToDeparture) {
        return PricingRules.applyChildPercentage(adultPrice(flight, dayToDeparture));
    }

    private Double adultPrice(Flight flight, int dayToDeparture) {
        PricingRules pricingRule = pricingRules.stream()
                .filter(daysToDepartureFilter(dayToDeparture))
                .findFirst().orElseThrow(() -> new RuntimeException("Unknown PricingRule."));

        return pricingRule.applyAdultPercentage(flight.getPrice());
    }
}
