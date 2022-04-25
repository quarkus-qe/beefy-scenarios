package io.quarkus.qe.validation;

import static io.quarkus.qe.validation.utils.ValidationAssertions.assertValidationErrorDetail;
import static io.quarkus.qe.validation.utils.ValidationAssertions.assertValidationErrorField;
import static io.quarkus.qe.validation.utils.ValidationAssertions.assertValidationErrorStatus;
import static io.quarkus.qe.validation.utils.ValidationAssertions.assertValidationErrorTitle;
import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.validation.utils.ValidationErrorResponse;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.Json;

@QuarkusTest
public class ValidationOnRequestBodyRouteHandlerTest {

    @Test
    public void shouldGetValidationErrorWhenRequestFirstCodeIsWrong() {
        Request request = new Request();
        request.setFirstCode("MA");

        ValidationErrorResponse response = given()
                .when()
                .body(Json.encode(request))
                .post("/validate/request-body")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ValidationErrorResponse.class);

        assertValidationErrorTitle(response);
        assertValidationErrorDetail(response);
        assertValidationErrorStatus(response, HttpStatus.SC_BAD_REQUEST);
        assertValidationErrorField(response, "validateRequestBody.param.firstCode", "First code must have 3 characters");
    }

    @Test
    public void shouldGetValidationErrorsWhenFirstAndSecondCodesAreWrong() {

        Request request = new Request();
        request.setFirstCode("MA");
        request.setSecondCode("F12");

        ValidationErrorResponse response = given()
                .body(Json.encode(request))
                .post("/validate/request-body")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ValidationErrorResponse.class);
        assertValidationErrorTitle(response);
        assertValidationErrorDetail(response);
        assertValidationErrorStatus(response, HttpStatus.SC_BAD_REQUEST);
        assertValidationErrorField(response, "validateRequestBody.param.firstCode", "First code must have 3 characters");
        assertValidationErrorField(response, "validateRequestBody.param.secondCode", "Second second must match pattern");
    }

    @Test
    public void shouldGetValidationErrorWhenSingleParamIsLowercase() {
        Request request = new Request();
        request.setFirstCode("MAD");
        request.setSecondCode("FR123");
        request.setCustom("lower");

        ValidationErrorResponse response = given()
                .when()
                .body(Json.encode(request))
                .post("/validate/request-body")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ValidationErrorResponse.class);

        assertValidationErrorTitle(response);
        assertValidationErrorDetail(response);
        assertValidationErrorStatus(response, HttpStatus.SC_BAD_REQUEST);
        assertValidationErrorField(response, "validateRequestBody.param.custom", "Value must be uppercase");
    }

    @Test
    public void shouldBeValidWhenTheRequestIsOk() {
        Request request = new Request();
        request.setFirstCode("MAD");
        request.setSecondCode("FR123");
        request.setCustom("UPPER");

        given().when()
                .body(Json.encode(request))
                .post("/validate/request-body")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
