package example.bookify.server.statistics.service;

import example.bookify.server.web.dto.request.Pagination;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StatisticsService<T> {

    Flux<T> findAll();

    Mono<Page<T>> findAll(Pagination pagination);

    Mono<T> save(T t);

    Class<T> getEntityClass();

    String getStatisticsFileName();
}
