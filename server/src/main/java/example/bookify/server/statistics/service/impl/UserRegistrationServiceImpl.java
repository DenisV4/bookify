package example.bookify.server.statistics.service.impl;

import lombok.RequiredArgsConstructor;
import example.bookify.server.statistics.model.UserRegistration;
import example.bookify.server.statistics.repository.UserRegistrationRepository;
import example.bookify.server.statistics.service.UserRegistrationService;
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
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRegistrationRepository userRegistrationRepository;

    @Override
    public Flux<UserRegistration> findAll() {
        return userRegistrationRepository.findAll();
    }

    @Override
    public Mono<Page<UserRegistration>> findAll(Pagination pagination) {
        var pageRequest = PageRequest.of(
                pagination.getPageNumber(),
                pagination.getPageSize(),
                Sort.by("timestamp").descending()
        );

        return userRegistrationRepository.findAllBy(pageRequest)
                .collectList()
                .zipWith(userRegistrationRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageRequest, p.getT2()));
    }

    @Override
    public Mono<UserRegistration> save(UserRegistration userRegistration) {
        return userRegistrationRepository.save(userRegistration);
    }

    @Override
    public Class<UserRegistration> getEntityClass() {
        return UserRegistration.class;
    }

    @Override
    public String getStatisticsFileName() {
        return "user-registrations";
    }
}
