package pl.srychert.SpringPizza.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TailLowerCaseValidator.class)
public @interface TailLowerCase {
    String message() default "Must be lowercase except for the first character";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
