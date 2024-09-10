package example.bookify.server.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingEvent {

    private Long roomId;

    private Long userId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;
}
