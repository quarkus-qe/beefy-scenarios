package io.quarkus.qe.vertx.sql.handlers.Flights;

import io.quarkus.qe.vertx.sql.domain.Basket;
import io.quarkus.qe.vertx.sql.domain.Flight;
import io.quarkus.qe.vertx.sql.domain.QueryFlightSearch;
import io.restassured.http.ContentType;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class FlightsHandlerTest {
    @Test
    @DisplayName("Retrieve all flights")
    public void retrieveAllFlights() {
        given().accept(ContentType.JSON)
                .when()
                .get("/flights/")
                .then()
                .statusCode(200)
                .assertThat().body("size()", is(89));
    }
    @Test
    @DisplayName("Retrieve all flights by origin and destination")
    public void retrieveFlightByOriginDestination() {
        Flight[] flights =  given().accept(ContentType.JSON)
                .when()
                .get("/flights/origin/MAD/destination/CDG")
                .then()
                .statusCode(200)
                .assertThat().body("size()", is(3))
                .extract().as(Flight[].class);

        assertThat(Arrays.asList(flights), hasItems(
                hasProperty("origin", is("MAD")),
                hasProperty("destination", is("CDG"))
        ));
    }
    @Test
    @DisplayName("Retrieve infant flights prices")
    public void retrieveInfantFlightPrices() {
        QueryFlightSearch query = QueryFlightSearch.Builder.newInstance()
                .withInfant(1)
                .withDaysToDeparture(1)
                .withFrom("MAD")
                .withTo("BCN")
                .build();

        List<Basket> basket = thenMakeFlightSearchQuery(query, 200,1);

        assertThat(basket, hasItems(
                hasProperty("flight", is("BA9569")),
                hasProperty("price", is(15.0))
        ));
    }
    @Test
    @DisplayName("Retrieve child flights prices")
    public void retrieveChildFlightPrices() {
        QueryFlightSearch query = QueryFlightSearch.Builder.newInstance()
                .withChild(1)
                .withDaysToDeparture(1)
                .withFrom("MAD")
                .withTo("BCN")
                .build();

        List<Basket> basket = thenMakeFlightSearchQuery(query, 200,1);

        assertThat(basket, hasItems(
                hasProperty("flight", is("BA9569")),
                hasProperty("price", is(233.16))
        ));
    }
    @Test
    @DisplayName("Retrieve adult flights prices")
    public void retrieveAdultFlightPrices() {
        QueryFlightSearch query = QueryFlightSearch.Builder.newInstance()
                .withAdult(1)
                .withDaysToDeparture(1)
                .withFrom("MAD")
                .withTo("BCN")
                .build();

        List<Basket> basket = thenMakeFlightSearchQuery(query, 200,1);

        assertThat(basket, hasItems(
                hasProperty("flight", is("BA9569")),
                hasProperty("price", is(348.0))
        ));
    }
    @Test
    @DisplayName("Retrieve flights prices")
    public void retrieveFlightPrices() {
        QueryFlightSearch query = QueryFlightSearch.Builder.newInstance()
                .withAdult(2)
                .withChild(2)
                .withInfant(1)
                .withDaysToDeparture(30)
                .withFrom("MAD")
                .withTo("BCN")
                .build();

        List<Basket> basket = thenMakeFlightSearchQuery(query, 200,1);

        assertThat(basket, hasItems(
                hasProperty("flight", is("BA9569")),
                hasProperty("price", is(789.88))
        ));
    }
    @Test
    @DisplayName("Retrieve multiple flights prices")
    public void retrieveMultiplesFlightPrices() {
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
                new Basket("IB7403", 855.02)
        );

        List<Basket> basket = thenMakeFlightSearchQuery(query, 200,3);
        assertEquals(basket, expectedBasket);
    }
    @Test
    @DisplayName("Wrong flight search format")
    public void wrongFlightSearchFormat() {
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
                .statusCode(400);
    }

    private List<Basket> thenMakeFlightSearchQuery(QueryFlightSearch query, int expectedStatus, int expectedAmount) {
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
