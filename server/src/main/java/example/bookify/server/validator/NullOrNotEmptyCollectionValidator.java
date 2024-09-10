package example.bookify.server.validator;

import example.bookify.server.validator.annotation.NullOrNotEmpty;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;

public class NullOrNotEmptyCollectionValidator implements ConstraintValidator<NullOrNotEmpty, Collection<?>> {

    @Override
    public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
        return value == null || !value.isEmpty();
    }
}
