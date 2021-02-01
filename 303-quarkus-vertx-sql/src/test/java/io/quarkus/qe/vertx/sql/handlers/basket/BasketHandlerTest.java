package io.quarkus.qe.vertx.sql.handlers.basket;

import io.quarkus.qe.vertx.sql.domain.Address;
import io.quarkus.qe.vertx.sql.domain.Basket;
import io.quarkus.qe.vertx.sql.domain.Passenger;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public abstract class BasketHandlerTest {
    @Test
    @DisplayName("basket checkout")
    public void basketCheckout() {
        Basket basket = new Basket();
        basket.setBillingPassenger(defaultPassenger());
        basket.setFlight("IB9961");
        basket.setPrice(437.52);

        given().accept(ContentType.JSON).body(basket)
                .when()
                .post("/basket/checkout")
                .then()
                .statusCode(201)
                .assertThat().body("isEmpty()", is(false));
    }

    @Test
    @DisplayName("wrong basket format checkout")
    public void wrongBasketFormatCheckout() {
        Basket basket = new Basket();
        basket.setBillingPassenger(defaultPassenger());
        basket.setFlight("IB9961");
        basket.setPrice(437.52);

        basket.getBillingPassenger().setNif(null);

        given().accept(ContentType.JSON).body(basket)
                .when()
                .post("/basket/checkout")
                .then()
                .statusCode(400);
    }

    private Passenger defaultPassenger() {
        Passenger passenger = new Passenger();
        passenger.setAddress(defaultAddress());
        passenger.setName("Walt");
        passenger.setLastName("White");
        passenger.setNif("14205723G");
        passenger.setContactNumber("+34608554433");

        return passenger;
    }

    private Address defaultAddress() {
        Address billingAddress = new Address();
        billingAddress.setStreet("Avd. del Tomillaron");
        billingAddress.setBlockNumber("4b");
        billingAddress.setZipCode("28080");
        billingAddress.setCity("Madrid");
        billingAddress.setCountry("Spain");

        return billingAddress;
    }
}
