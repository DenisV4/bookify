package example.bookify.server.validator.annotation;

import example.bookify.server.validator.CheckOutDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckOutDateValidator.class)
public @interface CheckOutAfterCheckIn {

    String message() default "Check-out date must be after check-in date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
