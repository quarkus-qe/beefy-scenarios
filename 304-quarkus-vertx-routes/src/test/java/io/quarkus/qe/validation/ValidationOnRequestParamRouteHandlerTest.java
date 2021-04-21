package io.quarkus.qe.validation;

import static io.quarkus.qe.validation.utils.ValidationAssertions.assertValidationErrorDetails;
import static io.quarkus.qe.validation.utils.ValidationAssertions.assertValidationErrorField;
import static io.quarkus.qe.validation.utils.ValidationAssertions.assertValidationErrorStatus;
import static io.quarkus.qe.validation.utils.ValidationAssertions.assertValidationErrorTitle;
import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.validation.utils.ValidationErrorResponse;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class ValidationOnRequestParamRouteHandlerTest {

    @Test
    public void shouldGetValidationErrorWhenSingleParamIsWrong() {
        // TODO: Added workaround ".accept(ContentType.JSON)" to get a JSON response.
        // Reported by https://github.com/quarkusio/quarkus/issues/15159
        ValidationErrorResponse response = given().accept(ContentType.JSON)
                .when()
                .get("/validate/request-single-param/MA")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ValidationErrorResponse.class);

        assertValidationErrorTitle(response);
        assertValidationErrorDetails(response);
        assertValidationErrorStatus(response, HttpStatus.SC_BAD_REQUEST);
        assertValidationErrorField(response, "validateRequestSingleParam.param", "Param must have 3 characters");
    }

    @Test
    public void shouldBeValidWhenSingleParamIsValid() {
        given().when()
                .get("/validate/request-single-param/MAD")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldGetValidationErrorsWhenAllParamsAreWrong() {
        // TODO: Added workaround ".accept(ContentType.JSON)" to get a JSON response.
        // Reported by https://github.com/quarkusio/quarkus/issues/15159
        ValidationErrorResponse response = given().accept(ContentType.JSON)
                .get("/validate/request-multiple-param/MA/second/F12")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ValidationErrorResponse.class);
        assertValidationErrorTitle(response);
        assertValidationErrorDetails(response);
        assertValidationErrorStatus(response, HttpStatus.SC_BAD_REQUEST);
        assertValidationErrorField(response, "validateRequestMultipleParam.firstParam", "First param must have 3 characters");
        assertValidationErrorField(response, "validateRequestMultipleParam.secondParam", "Second param must match pattern");
    }

    @Test
    public void shouldBeValidWhenMultipleParamsAreValid() {
        given().when()
                .get("/validate/request-multiple-param/MAD/second/FR123")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldGetValidationErrorWhenSingleParamIsLowercase() {
        // TODO: Added workaround ".accept(ContentType.JSON)" to get a JSON response.
        // Reported by https://github.com/quarkusio/quarkus/issues/15159
        ValidationErrorResponse response = given().accept(ContentType.JSON)
                .when()
                .get("/validate/request-single-param-custom-validation/lower")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ValidationErrorResponse.class);

        assertValidationErrorTitle(response);
        assertValidationErrorDetails(response);
        assertValidationErrorStatus(response, HttpStatus.SC_BAD_REQUEST);
        assertValidationErrorField(response, "validateRequestSingleParamUsingCustomValidation.param",
                "Value must be uppercase");
    }

    @Test
    public void shouldBeValidWhenSingleParamIsUppercase() {
        given().when()
                .get("/validate/request-single-param-custom-validation/UPPER")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
