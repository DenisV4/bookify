package example.bookify.server.service.impl;

import example.bookify.server.event.BookingEvent;
import example.bookify.server.exception.BookingException;
import example.bookify.server.mapper.BookingMapper;
import example.bookify.server.model.Booking;
import example.bookify.server.model.Room;
import example.bookify.server.repository.BookingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("BookingService Unit Tests")
@ExtendWith(MockitoExtension.class)
class BookingServiceImplTests {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private KafkaTemplate<String, BookingEvent> kafkaTemplate;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    @DisplayName("Should successfully book room when no overlap with any existing booking")
    void shouldBookRoomWhenNoOverlap() {
        var roomId = 1L;
        var newBooking = createBooking(
                roomId,
                LocalDate.of(2024, 9, 5),
                LocalDate.of(2024, 9, 16)
        );
        var existingBooking1 = createBooking(
                roomId,
                LocalDate.of(2024, 9, 1),
                LocalDate.of(2024, 9, 5)
        );
        var existingBooking2 = createBooking(roomId,
                LocalDate.of(2024, 9, 16),
                LocalDate.of(2024, 9, 20)
        );

        when(bookingRepository.findByRoomId(roomId)).thenReturn(List.of(existingBooking1, existingBooking2));

        var savedBooking = createBooking(
                roomId,
                LocalDate.of(2024, 9, 5),
                LocalDate.of(2024, 9, 16)
        );

        when(bookingRepository.save(newBooking)).thenReturn(savedBooking);

        var bookingEvent = new BookingEvent();
        when(bookingMapper.bookingToBookingEvent(newBooking)).thenReturn(bookingEvent);

        Booking result = bookingService.book(newBooking);

        assertThat(result).isEqualTo(savedBooking);

        verify(bookingRepository).save(newBooking);
        verify(kafkaTemplate).send(any(), eq(bookingEvent));
    }

    @Test
    @DisplayName("Should throw BookingException when there is full overlap with an existing booking")
    void shouldThrowBookingExceptionWhenFullOverlap() {
        var roomId = 1L;
        var newBooking = createBooking(
                roomId,
                LocalDate.of(2024, 9, 3),
                LocalDate.of(2024, 9, 6)
        );
        var existingBooking = createBooking(
                roomId,
                LocalDate.of(2024, 9, 1),
                LocalDate.of(2024, 9, 7)
        );

        when(bookingRepository.findByRoomId(roomId)).thenReturn(List.of(existingBooking));

        assertThatThrownBy(() -> bookingService.book(newBooking))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("Unable to book room with id=" + roomId);

        verify(bookingRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(any(), any(BookingEvent.class));
    }

    @Test
    @DisplayName("Should throw BookingException when there is partial overlap at the start")
    void shouldThrowBookingExceptionWhenPartialOverlapStart() {
        var roomId = 1L;
        var newBooking = createBooking(
                roomId,
                LocalDate.of(2024, 9, 5),
                LocalDate.of(2024, 9, 10)
        );
        var existingBooking = createBooking(
                roomId,
                LocalDate.of(2024, 9, 1),
                LocalDate.of(2024, 9, 7)
        );

        when(bookingRepository.findByRoomId(roomId)).thenReturn(List.of(existingBooking));

        assertThatThrownBy(() -> bookingService.book(newBooking))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("Unable to book room with id=" + roomId);

        verify(bookingRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(any(), any(BookingEvent.class));
    }

    @Test
    @DisplayName("Should throw BookingException when there is partial overlap at the end")
    void shouldThrowBookingExceptionWhenPartialOverlapEnd() {
        var roomId = 1L;
        var newBooking = createBooking(
                roomId,
                LocalDate.of(2024, 9, 3),
                LocalDate.of(2024, 9, 10)
        );
        var existingBooking = createBooking(roomId,
                LocalDate.of(2024, 9, 8),
                LocalDate.of(2024, 9, 15)
        );

        when(bookingRepository.findByRoomId(roomId)).thenReturn(List.of(existingBooking));

        assertThatThrownBy(() -> bookingService.book(newBooking))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("Unable to book room with id=" + roomId);

        verify(bookingRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(any(), any(BookingEvent.class));
    }

    private Booking createBooking(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        return Booking.builder()
                .room(Room.builder().id(roomId).build())
                .checkInDate(checkIn)
                .checkOutDate(checkOut)
                .build();
    }
}
