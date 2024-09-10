package example.bookify.server.validator.annotation;

import example.bookify.server.validator.NullOrNotBlankValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullOrNotBlankValidator.class)
public @interface NullOrNotBlank {

    String message() default "Value must be null or should not be blank";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
