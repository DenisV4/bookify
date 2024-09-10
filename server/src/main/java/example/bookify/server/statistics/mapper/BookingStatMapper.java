package example.bookify.server.statistics.mapper;

import example.bookify.server.event.BookingEvent;
import example.bookify.server.statistics.model.BookingStat;
import example.bookify.server.statistics.web.dto.BookingStatPageResponse;
import example.bookify.server.statistics.web.dto.BookingStatResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingStatMapper {

    BookingStat bookingEventToBookingStat(BookingEvent event);

    BookingStatResponse bookingStatToBookingStatResponse(BookingStat bookingStat);

    List<BookingStatResponse> bookingListToBookingStatResponseList(List<BookingStat> bookingStats);

    @Mapping(target = "totalElements", expression = "java(page.getTotalElements())")
    @Mapping(target = "totalPages", expression = "java(page.getTotalPages())")
    @Mapping(
            target = "bookings",
            expression = "java(bookingListToBookingStatResponseList(page.getContent()))"
    )
    BookingStatPageResponse bookingPageToBookingStatPageResponse(Page<BookingStat> page);
}
