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

@QuarkusTest
public class ValidationOnResponseRouteHandlerTest {

    @Test
    public void shouldBeValidWhenUsingUni() {
        given().when()
                .get("/validate/response-uni-valid")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldGetValidationErrorWhenUniResponseIdIsWrong() {
        ValidationErrorResponse response = given()
                .when()
                .get("/validate/response-uni-invalid-id")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .extract().as(ValidationErrorResponse.class);

        assertValidationErrorTitle(response);
        assertValidationErrorDetail(response);
        assertValidationErrorStatus(response, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        assertValidationErrorField(response, "id", "id can't be null");
    }
}
