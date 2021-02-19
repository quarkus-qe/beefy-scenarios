package io.quarkus.qe.validation.annotations;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@ApplicationScoped
public class UppercaseValidator implements ConstraintValidator<Uppercase, String> {

    @Inject
    UppercaseService uppercaseService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return uppercaseService.isUppercase(value);
    }
}
