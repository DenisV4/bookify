package example.bookify.server.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import example.bookify.server.validator.annotation.CheckOutAfterCheckIn;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
@CheckOutAfterCheckIn
@Schema(
        title = "Booking request",
        description = "Insert new booking"
)
public class BookingRequest implements RequestWithCheckInAndCheckOutDates {

    @NotNull(message = "'roomId' {notNull.message}")
    @Positive(message = "'roomId {positive.message}")
    private Long roomId;

    @NotNull(message = "'userId' {notNull.message}")
    @Positive(message = "'userId {positive.message}")
    private Long userId;

    @NotNull(message = "'checkInDate' {notNull.message}")
    @FutureOrPresent(message = "'checkInDate' {futureOrPresent.message}")
    private LocalDate checkInDate;

    @NotNull(message = "'checkOutDate' {notNull.message}")
    @FutureOrPresent(message = "'checkOutDate' {futureOrPresent.message}")
    private LocalDate checkOutDate;
}
