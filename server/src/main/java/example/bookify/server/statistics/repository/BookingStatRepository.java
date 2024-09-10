package example.bookify.server.statistics.repository;

import lombok.NonNull;
import example.bookify.server.statistics.model.BookingStat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingStatRepository extends
        ReactiveMongoRepository<BookingStat, String>,
        ReactiveSortingRepository<BookingStat, String> {

    @NonNull Mono<Long> count();

    Flux<BookingStat> findAllBy(Pageable pageable);
}
