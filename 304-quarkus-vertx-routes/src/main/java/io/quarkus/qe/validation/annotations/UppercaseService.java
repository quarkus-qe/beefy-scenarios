package io.quarkus.qe.validation.annotations;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UppercaseService {
    private static final String UPPERCASE_PATTERN = "[A-Z]+";

    public boolean isUppercase(String value) {
        return value != null && value.matches(UPPERCASE_PATTERN);
    }
}
