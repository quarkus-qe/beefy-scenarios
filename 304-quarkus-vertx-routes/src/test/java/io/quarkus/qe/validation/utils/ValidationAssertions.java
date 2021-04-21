package io.quarkus.qe.validation.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ValidationAssertions {
    private ValidationAssertions() {

    }

    public static void assertValidationErrorTitle(ValidationErrorResponse response) {
        assertEquals("Constraint Violation", response.getTitle());
    }

    public static void assertValidationErrorDetails(ValidationErrorResponse response) {
        assertEquals("validation constraint violations", response.getDetails());
    }

    public static void assertValidationErrorStatus(ValidationErrorResponse response, int expected) {
        assertEquals(expected, response.getStatus());
    }

    public static void assertValidationErrorCount(ValidationErrorResponse response, int expected) {
        assertEquals(expected, response.getViolations().length,
                "Expected " + expected + " errors. Found " + response.getViolations().length);
    }

    public static void assertValidationErrorField(ValidationErrorResponse response, String fieldName, String message) {
        List<ValidationError> violations = Stream.of(response.getViolations()).filter(v -> fieldName.equals(v.getField()))
                .collect(Collectors.toList());
        assertFalse(violations.isEmpty(), "No violations found for field: " + fieldName);
        assertEquals(1, violations.size(), "More than one violation found for field: " + fieldName);
        assertEquals(message, violations.get(0).getMessage());
    }

}
