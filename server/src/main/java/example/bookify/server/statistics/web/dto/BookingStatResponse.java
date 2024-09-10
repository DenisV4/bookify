package example.bookify.server.statistics.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "Booking statistics response")
public class BookingStatResponse {

    private LocalDateTime timestamp;

    private Long roomId;

    private Long userId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;
}
