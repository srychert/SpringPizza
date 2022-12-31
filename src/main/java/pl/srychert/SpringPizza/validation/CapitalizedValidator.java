package pl.srychert.SpringPizza.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CapitalizedValidator implements ConstraintValidator<Capitalized, String> {
    public void initialize(Capitalized constraint) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        char first = name.charAt(0);
        return Character.isUpperCase(first);
    }
}
