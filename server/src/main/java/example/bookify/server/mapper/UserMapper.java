package example.bookify.server.mapper;

import example.bookify.server.event.UserRegistrationEvent;
import example.bookify.server.model.User;
import example.bookify.server.web.dto.request.UserUpsertRequest;
import example.bookify.server.web.dto.response.UserPageResponse;
import example.bookify.server.web.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = UserRoleMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    protected PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Mapping(
            target = "password",
            expression = "java(request.getPassword() == null ? null : passwordEncoder.encode(request.getPassword()))"
    )
    @Mapping(target = "roles", expression = "java(null)")
    @Mapping(target = "bookings", expression = "java(null)")
    public abstract User requestToUser(UserUpsertRequest request);

    @Mapping(target = "id", source = "userId")
    @Mapping(
            target = "password",
            expression = "java(request.getPassword() == null ? null : passwordEncoder.encode(request.getPassword()))"
    )
    @Mapping(target = "roles", expression = "java(null)")
    @Mapping(target = "bookings", expression = "java(null)")
    public abstract User requestToUser(Long userId, UserUpsertRequest request);

    public abstract UserResponse userToResponse(User user);

    @Mapping(target = "userId", source = "id")
    public abstract UserRegistrationEvent userToUserRegistrationEvent(User user);

    public abstract List<UserResponse> userListToUserResponseList(List<User> users);

    @Mapping(target = "totalElements", expression = "java(userPage.getTotalElements())")
    @Mapping(target = "totalPages", expression = "java(userPage.getTotalPages())")
    @Mapping(target = "users", expression = "java(userListToUserResponseList(userPage.getContent()))")
    public abstract UserPageResponse userPageToUserPageResponse(Page<User> userPage);
}
