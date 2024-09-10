package example.bookify.server.security.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthTokens {

    private String accessToken;

    private String refreshToken;
}
