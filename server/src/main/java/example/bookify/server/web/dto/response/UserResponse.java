package example.bookify.server.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.jackson.Jacksonized;
import example.bookify.server.model.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Jacksonized
@Builder
@Getter
@Setter
@Schema(
        title = "User response",
        description = "User record"
)
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private List<UserRole.RoleType> roles;
}
