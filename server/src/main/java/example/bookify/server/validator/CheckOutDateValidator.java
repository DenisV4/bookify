package example.bookify.server.validator;

import example.bookify.server.validator.annotation.CheckOutAfterCheckIn;
import example.bookify.server.web.dto.request.RequestWithCheckInAndCheckOutDates;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckOutDateValidator implements ConstraintValidator<CheckOutAfterCheckIn, RequestWithCheckInAndCheckOutDates> {

    @Override
    public boolean isValid(RequestWithCheckInAndCheckOutDates value, ConstraintValidatorContext context) {
        var checkInDate = value.getCheckInDate();
        var checkOutDate = value.getCheckOutDate();

        if (checkInDate == null || checkOutDate == null) {
            return true;
        }

        return checkOutDate.isAfter(checkInDate);
    }
}
