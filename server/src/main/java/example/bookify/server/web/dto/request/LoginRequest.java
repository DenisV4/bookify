package example.bookify.server.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        title = "Login request"
)
public class LoginRequest {
    @NotNull(message = "'name' {notNull.message}")
    @NotBlank(message = "'name' {notBlank.message}")
    @Size(min = 3, max = 30, message = "'name' {size.message}")
    private String name;

    @NotNull(message = "'password' {notNull.message}")
    @NotBlank(message = "'password' {notBlank.message}")
    @Size(min = 3, max = 30, message = "'password' {size.message}")
    private String password;
}
