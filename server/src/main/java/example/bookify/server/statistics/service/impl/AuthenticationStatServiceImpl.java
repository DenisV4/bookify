package example.bookify.server.statistics.service.impl;

import lombok.RequiredArgsConstructor;
import example.bookify.server.statistics.model.AuthenticationStat;
import example.bookify.server.statistics.repository.AuthenticationStatRepository;
import example.bookify.server.statistics.service.AuthenticationStatService;
import example.bookify.server.web.dto.request.Pagination;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthenticationStatServiceImpl implements AuthenticationStatService {

    private final AuthenticationStatRepository authenticationStatRepository;

    @Override
    public Flux<AuthenticationStat> findAll() {
        return authenticationStatRepository.findAll();
    }

    @Override
    public Mono<Page<AuthenticationStat>> findAll(Pagination pagination) {
        Pageable pageRequest = PageRequest.of(
                pagination.getPageNumber(),
                pagination.getPageSize(),
                Sort.by("timestamp").descending()
        );

        return authenticationStatRepository.findAllBy(pageRequest)
                .collectList()
                .zipWith(authenticationStatRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageRequest, p.getT2()));
    }

    @Override
    public Mono<AuthenticationStat> save(AuthenticationStat authenticationStat) {
        return authenticationStatRepository.save(authenticationStat);
    }

    @Override
    public Class<AuthenticationStat> getEntityClass() {
        return AuthenticationStat.class;
    }

    @Override
    public String getStatisticsFileName() {
        return "authentications";
    }
}
