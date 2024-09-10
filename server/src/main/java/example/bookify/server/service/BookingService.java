package example.bookify.server.service;

import example.bookify.server.model.Booking;
import example.bookify.server.web.dto.request.Pagination;
import org.springframework.data.domain.Page;

public interface BookingService {

    Page<Booking> findAll(Pagination pagination);

    Booking book(Booking booking);
}
