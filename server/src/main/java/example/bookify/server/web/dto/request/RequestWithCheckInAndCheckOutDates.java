package example.bookify.server.web.dto.request;

import java.time.LocalDate;

public interface RequestWithCheckInAndCheckOutDates {

    LocalDate getCheckInDate();

    LocalDate getCheckOutDate();
}
