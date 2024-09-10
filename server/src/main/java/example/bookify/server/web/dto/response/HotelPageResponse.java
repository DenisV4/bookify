package example.bookify.server.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@SuperBuilder
@Getter
@Setter
@Schema(title = "Hotel page")
public class HotelPageResponse extends AbstractPageResponse {

    private List<HotelResponse> hotels;
}
