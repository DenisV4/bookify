package example.bookify.server.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Schema(
        title = "Error response"
)
public class ErrorResponse {

    private String errorMessage;
}
