package example.bookify.server.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Jacksonized
@Builder
@Getter
@Setter
@Schema(
        title = "Booking response",
        description = "Booking record"
)
public class BookingResponse {

    private Long id;

    private Long roomId;

    private Long userId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;
}
