package io.quarkus.qe.validation.annotations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@ApplicationScoped
public class UppercaseValidator implements ConstraintValidator<Uppercase, String> {

    @Inject
    UppercaseService uppercaseService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return uppercaseService.isUppercase(value);
    }
}
