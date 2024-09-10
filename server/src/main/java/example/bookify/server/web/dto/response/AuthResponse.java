package example.bookify.server.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Data
@Schema(
        title = "Authentication response"
)
public class AuthResponse {

    private String accessToken;
}
