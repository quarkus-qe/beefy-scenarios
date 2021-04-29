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
    default void retrieveAllFlights() {
        given().accept(ContentType.JSON)
                .when()
                .get("/flights/")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .assertThat().body("size()", is(89));
    }

    default void retrieveFlightByOriginDestination() {
        Flight[] flights = given().accept(ContentType.JSON)
                .when()
                .get("/flights/origin/MAD/destination/CDG")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .assertThat().body("size()", is(3))
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
                hasProperty("price", is(15.0))));
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
                hasProperty("price", is(233.16))));
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
                hasProperty("price", is(348.0))));
    }

    default void retrieveFlightPrices() {
        QueryFlightSearch query = QueryFlightSearch.Builder.newInstance()
                .withAdult(2)
                .withChild(2)
                .withInfant(1)
                .withDaysToDeparture(30)
                .withFrom("MAD")
                .withTo("BCN")
                .build();

        List<Basket> basket = thenMakeFlightSearchQuery(query, HttpResponseStatus.OK.code(), 1);

        assertThat(basket, hasItems(
                hasProperty("flight", is("BA9569")),
                hasProperty("price", is(789.88))));
    }

    default void retrieveMultiplesFlightPrices() {
        QueryFlightSearch query = QueryFlightSearch.Builder.newInstance()
                .withAdult(2)
                .withChild(2)
                .withInfant(1)
                .withDaysToDeparture(30)
                .withFrom("MAD")
                .withTo("CDG")
                .build();

        List<Basket> expectedBasket = Arrays.asList(
                new Basket("IB9961", 437.52),
                new Basket("IB6112", 384.08000000000004),
                new Basket("IB7403", 855.02));

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
