package example.bookify.server.statistics.service.impl;

import example.bookify.server.statistics.model.BookingStat;
import example.bookify.server.statistics.repository.BookingStatRepository;
import example.bookify.server.statistics.service.BookingStatService;
import lombok.RequiredArgsConstructor;
import example.bookify.server.web.dto.request.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookingStatServiceImpl implements BookingStatService {

    private final BookingStatRepository bookingStatRepository;

    @Override
    public Flux<BookingStat> findAll() {
        return bookingStatRepository.findAll();
    }

    @Override
    public Mono<Page<BookingStat>> findAll(Pagination pagination) {
        var pageRequest = PageRequest.of(
                pagination.getPageNumber(),
                pagination.getPageSize(),
                Sort.by("timestamp").descending()
        );

        return bookingStatRepository.findAllBy(pageRequest)
                .collectList()
                .zipWith(bookingStatRepository.count())
                .map(it -> new PageImpl<>(it.getT1(), pageRequest, it.getT2()));
    }

    @Override
    public Mono<BookingStat> save(BookingStat bookingStat) {
        return bookingStatRepository.save(bookingStat);
    }

    @Override
    public Class<BookingStat> getEntityClass() {
        return BookingStat.class;
    }

    @Override
    public String getStatisticsFileName() {
        return "bookings";
    }
}
