package io.quarkus.qe.vertx.sql.handlers;

import org.junit.jupiter.api.Test;

import io.quarkus.qe.vertx.sql.handlers.spec.AirlineHandlerSpec;
import io.quarkus.qe.vertx.sql.handlers.spec.AirportHandlerSpec;
import io.quarkus.qe.vertx.sql.handlers.spec.BasketHandlerSpec;
import io.quarkus.qe.vertx.sql.handlers.spec.FlightsHandlerSpec;
import io.quarkus.qe.vertx.sql.handlers.spec.PricingRulesSpec;

public abstract class CommonTestCases implements
        PricingRulesSpec, FlightsHandlerSpec, BasketHandlerSpec, AirportHandlerSpec, AirlineHandlerSpec {

    @Test
    public void pricingRuleScenario() {
        retrieveAllPricingRules();
    }

    @Test
    public void flightScenario() {
        retrieveAllFlights();
        retrieveFlightByOriginDestination();
        retrieveInfantFlightPrices();
        retrieveChildFlightPrices();
        retrieveAdultFlightPrices();
        retrieveFlightPrices();
        retrieveMultiplesFlightPrices();
        wrongFlightSearchFormat();
    }

    @Test
    public void basketScenario() {
        basketCheckout();
        // TODO: Validation stopped being propagated onto error handler. Reported by https://github.com/quarkusio/quarkus/issues/16430
        // wrongBasketFormatCheckout();
    }

    @Test
    public void airportScenario() {
        retrieveAllAirports();
    }

    @Test
    public void airlineScenario() {
        retrieveAllAirlines();
    }
}
