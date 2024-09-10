package example.bookify.server.validator;

import example.bookify.server.validator.annotation.NullOrNotEmpty;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullOrNotEmptyStringValidator implements ConstraintValidator<NullOrNotEmpty, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        return value == null || !value.toString().isBlank();
    }
}
