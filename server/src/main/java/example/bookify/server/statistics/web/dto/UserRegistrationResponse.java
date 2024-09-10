package example.bookify.server.statistics.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Jacksonized
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "User registration statistics response")
public class UserRegistrationResponse {

    private LocalDateTime timestamp;

    private Long userId;

    private String name;

    private String email;
}
