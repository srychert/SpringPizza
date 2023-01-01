package pl.srychert.SpringPizza.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TailLowerCaseValidator implements ConstraintValidator<TailLowerCase, String> {
    public void initialize(TailLowerCase constraint) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (name == null || name.length() < 1) {
            return false;
        }
        String tail = name.substring(1);
        return tail.toLowerCase().equals(tail);
    }
}
