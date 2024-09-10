package example.bookify.server.statistics.web.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import example.bookify.server.web.dto.response.AbstractPageResponse;

import java.util.List;

@Jacksonized
@SuperBuilder
@Getter
@Setter
@Schema(title = "User registration statistics page")
public class UserRegistrationPageResponse extends AbstractPageResponse {

    private List<UserRegistrationResponse> registrations;
}
