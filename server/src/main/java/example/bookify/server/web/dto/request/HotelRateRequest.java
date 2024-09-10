package example.bookify.server.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(
        title = "Hotel rate request",
        description = "Rate hotel"
)
public class HotelRateRequest {

    @NotNull(message = "'score' {notNull.message}")
    @Min(value = 1, message = "'score' {min.message}")
    @Max(value = 5, message = "'score' {max.message}")
    private Integer score;
}
