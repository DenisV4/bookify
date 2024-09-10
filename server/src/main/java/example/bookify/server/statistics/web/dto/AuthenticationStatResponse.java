package example.bookify.server.statistics.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "Authentication statistics response")
public class AuthenticationStatResponse {

    private LocalDateTime timestamp;

    private Long userId;

    private String name;

    private String device;

    private String os;

    private String browser;

    private String remoteAddress;
}
