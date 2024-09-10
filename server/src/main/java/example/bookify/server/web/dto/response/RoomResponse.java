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
        title = "Room response",
        description = "Room record"
)
public class RoomResponse {

    private Long id;

    private String name;

    private String description;

    private String number;

    private Float price;

    private Integer guestsNumber;

    private Long hotelId;

    private String hotelName;

    private String city;

    private String address;
}
