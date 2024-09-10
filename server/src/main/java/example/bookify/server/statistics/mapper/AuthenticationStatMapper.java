package example.bookify.server.statistics.mapper;

import example.bookify.server.event.AuthenticationEvent;
import example.bookify.server.statistics.model.AuthenticationStat;
import example.bookify.server.statistics.web.dto.AuthenticationStatResponse;
import example.bookify.server.statistics.web.dto.AuthenticationStatPageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthenticationStatMapper {

    AuthenticationStat authenticationEventToAuthenticationStat(AuthenticationEvent event);

    AuthenticationStatResponse authenticationStatToAuthenticationStatResponse(AuthenticationStat authenticationStat);

    List<AuthenticationStatResponse> authenticationStatListToAuthenticationStatResponseList(List<AuthenticationStat> authenticationStats);

    @Mapping(target = "totalElements", expression = "java(page.getTotalElements())")
    @Mapping(target = "totalPages", expression = "java(page.getTotalPages())")
    @Mapping(
            target = "logins",
            expression = "java(authenticationStatListToAuthenticationStatResponseList(page.getContent()))"
    )
    AuthenticationStatPageResponse authenticationPageToAuthenticationStatPageResponse(Page<AuthenticationStat> page);
}
