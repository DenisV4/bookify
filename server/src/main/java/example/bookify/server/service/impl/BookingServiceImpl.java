package example.bookify.server.service.impl;

import example.bookify.server.event.BookingEvent;
import example.bookify.server.exception.BookingException;
import example.bookify.server.mapper.BookingMapper;
import example.bookify.server.model.Booking;
import example.bookify.server.repository.BookingRepository;
import example.bookify.server.service.BookingService;
import example.bookify.server.web.dto.request.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    @Value("${app.kafka.topic.statistics.booking}")
    private String topic;

    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Cacheable(value = "bookings", keyGenerator = "keyGenerator")
    public Page<Booking> findAll(Pagination pagination) {
        var pageRequest = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(), Sort.by("id"));
        return bookingRepository.findAll(pageRequest);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "bookings", allEntries = true),
            @CacheEvict(value = "rooms", allEntries = true)
    })
    public Booking book(Booking booking) {
        var existingBookings = bookingRepository.findByRoomId(booking.getRoom().getId());
        var isBookingPossible = existingBookings.stream()
                .noneMatch(dateOverlapPredicate(booking));

        if (!isBookingPossible) {
            throw new BookingException(
                    "Unable to book room with id={0} on the specified dates", booking.getRoom().getId());
        }

        var createdBooking = bookingRepository.save(booking);
        kafkaTemplate.send(topic, bookingMapper.bookingToBookingEvent(booking));

        return createdBooking;
    }

    private Predicate<Booking> dateOverlapPredicate(Booking newBooking) {
        return existingBooking -> {
            var existingCheckInDate = existingBooking.getCheckInDate();
            var existingCheckOutDate = existingBooking.getCheckOutDate();
            var newCheckInDate = newBooking.getCheckInDate();
            var newCheckOutDate = newBooking.getCheckOutDate();

            return newCheckInDate.isBefore(existingCheckOutDate)
                    && newCheckOutDate.isAfter(existingCheckInDate)
                    && !newCheckInDate.isEqual(existingCheckOutDate)
                    && !newCheckOutDate.isEqual(existingCheckInDate);
        };
    }
}
