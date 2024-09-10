package example.bookify.server.statistics.repository;

import lombok.NonNull;
import example.bookify.server.statistics.model.UserRegistration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRegistrationRepository extends
        ReactiveMongoRepository<UserRegistration, String>,
        ReactiveSortingRepository<UserRegistration, String> {

    @NonNull Mono<Long> count();

    Flux<UserRegistration> findAllBy(Pageable pageable);
}
