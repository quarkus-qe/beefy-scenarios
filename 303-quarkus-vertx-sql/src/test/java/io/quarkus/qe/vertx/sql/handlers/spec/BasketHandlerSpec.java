package io.quarkus.qe.vertx.sql.handlers.spec;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.qe.vertx.sql.domain.Address;
import io.quarkus.qe.vertx.sql.domain.Basket;
import io.quarkus.qe.vertx.sql.domain.Passenger;
import io.restassured.http.ContentType;

public interface BasketHandlerSpec {
    default void basketCheckout() {
        Basket basket = new Basket();
        basket.setBillingPassenger(defaultPassenger());
        basket.setFlight("IB9961");
        basket.setPrice(437.52);

        given().accept(ContentType.JSON).body(basket)
                .when()
                .post("/basket/checkout")
                .then()
                .statusCode(HttpResponseStatus.CREATED.code())
                .assertThat().body("isEmpty()", is(false));
    }

    default void wrongBasketFormatCheckout() {
        Basket basket = new Basket();
        basket.setBillingPassenger(defaultPassenger());
        basket.setFlight("IB9961");
        basket.setPrice(437.52);

        basket.getBillingPassenger().setNif(null);

        given().accept(ContentType.JSON).body(basket)
                .when()
                .post("/basket/checkout")
                .then()
                .statusCode(HttpResponseStatus.BAD_REQUEST.code());
    }

    default Passenger defaultPassenger() {
        Passenger passenger = new Passenger();
        passenger.setAddress(defaultAddress());
        passenger.setName("Walt");
        passenger.setLastName("White");
        passenger.setNif("14205723G");
        passenger.setContactNumber("+34608554433");

        return passenger;
    }

    default Address defaultAddress() {
        Address billingAddress = new Address();
        billingAddress.setStreet("Avd. del Tomillaron");
        billingAddress.setBlockNumber("4b");
        billingAddress.setZipCode("28080");
        billingAddress.setCity("Madrid");
        billingAddress.setCountry("Spain");

        return billingAddress;
    }
}
