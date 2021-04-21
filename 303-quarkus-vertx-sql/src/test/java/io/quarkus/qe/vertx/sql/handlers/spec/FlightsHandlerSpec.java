package io.quarkus.qe.vertx.sql.handlers.spec;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.qe.vertx.sql.domain.Basket;
import io.quarkus.qe.vertx.sql.domain.Flight;
import io.quarkus.qe.vertx.sql.domain.QueryFlightSearch;
import io.restassured.http.ContentType;

public interface FlightsHandlerSpec {

    int EXPECTED_FLIGHTS_COUNT = 89;
    int EXPECTED_MAD_TO_CDG_FLIGHTS_COUNT = 3;
    double EXPECTED_MAD_TO_BCN_WITH_INFANT_PRICE = 15.0;
    double EXPECTED_MAD_TO_BCN_WITH_CHILD_PRICE = 233.16;
    double EXPECTED_MAD_TO_BCN_WITH_ADULT_PRICE = 348.0;
    double EXPECTED_MAD_TO_BCN_COMPLEX_QUERY_PRICE = 789.88;
    double IB9961_PRICE = 437.52;
    double IB6112_PRICE = 384.08000000000004;
    double IB7403_PRICE = 855.02;
    int THIRTY = 30;

    default void retrieveAllFlights() {
        given().accept(ContentType.JSON)
                .when()
                .get("/flights/")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .assertThat().body("size()", is(EXPECTED_FLIGHTS_COUNT));
    }

    default void retrieveFlightByOriginDestination() {
        Flight[] flights = given().accept(ContentType.JSON)
                .when()
                .get("/flights/origin/MAD/destination/CDG")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .assertThat().body("size()", is(EXPECTED_MAD_TO_CDG_FLIGHTS_COUNT))
                .extract().as(Flight[].class);

        assertThat(Arrays.asList(flights), hasItems(
                hasProperty("origin", is("MAD")),
                hasProperty("destination", is("CDG"))));
    }

    default void retrieveInfantFlightPrices() {
        QueryFlightSearch query = QueryFlightSearch.Builder.newInstance()
                .withInfant(1)
                .withDaysToDeparture(1)
                .withFrom("MAD")
                .withTo("BCN")
                .build();

        List<Basket> basket = thenMakeFlightSearchQuery(query, HttpResponseStatus.OK.code(), 1);

        assertThat(basket, hasItems(
                hasProperty("flight", is("BA9569")),
                hasProperty("price", is(EXPECTED_MAD_TO_BCN_WITH_INFANT_PRICE))));
    }

    default void retrieveChildFlightPrices() {
        QueryFlightSearch query = QueryFlightSearch.Builder.newInstance()
                .withChild(1)
                .withDaysToDeparture(1)
                .withFrom("MAD")
                .withTo("BCN")
                .build();

        List<Basket> basket = thenMakeFlightSearchQuery(query, HttpResponseStatus.OK.code(), 1);

        assertThat(basket, hasItems(
                hasProperty("flight", is("BA9569")),
                hasProperty("price", is(EXPECTED_MAD_TO_BCN_WITH_CHILD_PRICE))));
    }

    default void retrieveAdultFlightPrices() {
        QueryFlightSearch query = QueryFlightSearch.Builder.newInstance()
                .withAdult(1)
                .withDaysToDeparture(1)
                .withFrom("MAD")
                .withTo("BCN")
                .build();

        List<Basket> basket = thenMakeFlightSearchQuery(query, HttpResponseStatus.OK.code(), 1);

        assertThat(basket, hasItems(
                hasProperty("flight", is("BA9569")),
                hasProperty("price", is(EXPECTED_MAD_TO_BCN_WITH_ADULT_PRICE))));
    }

    default void retrieveFlightPrices() {
        QueryFlightSearch query = QueryFlightSearch.Builder.newInstance()
                .withAdult(2)
                .withChild(2)
                .withInfant(1)
                .withDaysToDeparture(THIRTY)
                .withFrom("MAD")
                .withTo("BCN")
                .build();

        List<Basket> basket = thenMakeFlightSearchQuery(query, HttpResponseStatus.OK.code(), 1);

        assertThat(basket, hasItems(
                hasProperty("flight", is("BA9569")),
                hasProperty("price", is(EXPECTED_MAD_TO_BCN_COMPLEX_QUERY_PRICE))));
    }

    default void retrieveMultiplesFlightPrices() {
        QueryFlightSearch query = QueryFlightSearch.Builder.newInstance()
                .withAdult(2)
                .withChild(2)
                .withInfant(1)
                .withDaysToDeparture(THIRTY)
                .withFrom("MAD")
                .withTo("CDG")
                .build();

        List<Basket> expectedBasket = Arrays.asList(
                new Basket("IB9961", IB9961_PRICE),
                new Basket("IB6112", IB6112_PRICE),
                new Basket("IB7403", IB7403_PRICE));

        List<Basket> basket = thenMakeFlightSearchQuery(query, HttpResponseStatus.OK.code(), 3);
        assertEquals(basket, expectedBasket);
    }

    default void wrongFlightSearchFormat() {
        QueryFlightSearch query = QueryFlightSearch.Builder.newInstance()
                .withInfant(1)
                .withDaysToDeparture(-1)
                .withFrom("MAD")
                .withTo("BCN")
                .build();

        given().accept(ContentType.JSON)
                .body(query.toJsonStringify())
                .when()
                .put("/flights/search")
                .then()
                .statusCode(HttpResponseStatus.BAD_REQUEST.code());
    }

    default List<Basket> thenMakeFlightSearchQuery(QueryFlightSearch query, int expectedStatus, int expectedAmount) {
        return Arrays.asList(given().accept(ContentType.JSON)
                .body(query.toJsonStringify())
                .when()
                .put("/flights/search")
                .then()
                .statusCode(expectedStatus)
                .assertThat().body("size()", is(expectedAmount))
                .extract().as(Basket[].class));
    }
}
