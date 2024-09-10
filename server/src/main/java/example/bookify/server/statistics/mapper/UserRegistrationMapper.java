package example.bookify.server.statistics.mapper;

import example.bookify.server.event.UserRegistrationEvent;
import example.bookify.server.statistics.model.UserRegistration;
import example.bookify.server.statistics.web.dto.UserRegistrationPageResponse;
import example.bookify.server.statistics.web.dto.UserRegistrationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRegistrationMapper {

    UserRegistration userRegistrationEventToUserRegistration(UserRegistrationEvent event);

    UserRegistrationResponse userRegistrationToUserRegistrationResponse(UserRegistration userRegistration);

    List<UserRegistrationResponse> userRegistrationListToUserRegistrationResponseList(List<UserRegistration> userRegistrations);

    @Mapping(target = "totalElements", expression = "java(page.getTotalElements())")
    @Mapping(target = "totalPages", expression = "java(page.getTotalPages())")
    @Mapping(
            target = "registrations",
            expression = "java(userRegistrationListToUserRegistrationResponseList(page.getContent()))"
    )
    UserRegistrationPageResponse userRegistrationPageToUserRegistrationPageResponse(Page<UserRegistration> page);
}
