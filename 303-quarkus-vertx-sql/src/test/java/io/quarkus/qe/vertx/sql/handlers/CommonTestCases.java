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
        wrongBasketFormatCheckout();
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
