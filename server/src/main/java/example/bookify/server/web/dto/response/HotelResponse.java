package example.bookify.server.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Getter
@Setter
@Schema(
    title = "Hotel response",
    description = "Hotel record"
)
public class HotelResponse {

    private Long id;

    private String name;

    private String title;

    private String city;

    private String address;

    private Float distance;

    private Float rating;

    private Integer ratingsCount;
}
