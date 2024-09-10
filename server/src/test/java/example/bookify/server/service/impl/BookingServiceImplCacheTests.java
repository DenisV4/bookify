package example.bookify.server.service.impl;

import example.bookify.server.event.BookingEvent;
import example.bookify.server.mapper.BookingMapper;
import example.bookify.server.model.Booking;
import example.bookify.server.model.Room;
import example.bookify.server.repository.BookingRepository;
import example.bookify.server.service.BookingService;
import example.bookify.server.web.dto.request.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("BookingService Cache Tests")
@ContextConfiguration(classes = {
        BookingService.class,
        BookingServiceImpl.class
})
class BookingServiceImplCacheTests extends AbstractServiceCacheTest {

    @Autowired
    private BookingService bookingService;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private KafkaTemplate<String, BookingEvent> kafkaTemplate;

    @MockBean
    private BookingMapper bookingMapper;

    @Test
    @DisplayName("Should cache the results of the findAll method")
    void shouldCacheResultsOfFindAllMethod() {
        var pagination = new Pagination();
        pagination.setPageNumber(0);
        pagination.setPageSize(10);

        var booking1 = Booking.builder().id(1L).build();
        var booking2 = Booking.builder().id(2L).build();
        var page = createPage(0, 10, List.of(booking1, booking2));

        when(bookingRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(page);

        assertCacheKeyCount(0);

        var result1 = bookingService.findAll(pagination);
        var result2 = bookingService.findAll(pagination);

        assertCacheContainsKey("bookings::Pagination(pageNumber=0, pageSize=10)");
        assertPagesEqual(result1, result2);

        verify(bookingRepository, times(1))
                .findAll(ArgumentMatchers.any(PageRequest.class));
    }

    @Test
    @DisplayName("Should evict 'bookings' and 'rooms' cache when a booking is created")
    void shouldEvictCacheWhenBookingIsCreated() {
        var room = Room.builder().id(1L).build();
        var booking = Booking.builder().room(room).build();
        var existingBookings = createPage(0, 10, List.of(booking));

        when(bookingRepository.findByRoomId(1L))
                .thenReturn(List.of());
        when(bookingRepository.save(ArgumentMatchers.any(Booking.class)))
                .thenReturn(booking);

        assertCacheKeyCount(0);

        populateCache("bookings::Pagination(pageNumber=0, pageSize=10)", existingBookings);
        populateCache("rooms::Pagination(pageNumber=0, pageSize=10)", List.of(room));

        assertCacheKeyCount(2);

        bookingService.book(booking);

        assertCacheDoesNotContainKey("bookings::*");
        assertCacheDoesNotContainKey("rooms::*");
    }
}
