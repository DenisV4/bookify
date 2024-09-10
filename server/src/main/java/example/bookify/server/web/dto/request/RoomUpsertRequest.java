package example.bookify.server.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import example.bookify.server.validator.annotation.NullOrNotBlank;
import example.bookify.server.validator.group.OnCreate;
import example.bookify.server.validator.group.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        title = "Room request",
        description = "Insert or update room"
)
public class RoomUpsertRequest {

    @NotNull(message = "'name' {notNull.message}", groups = OnCreate.class)
    @NotBlank(message = "'name' {notBlank.message}", groups = OnCreate.class)
    @NullOrNotBlank(message = "'name' {nullOrNotBlank.message}", groups = OnUpdate.class)
    @Size(min = 3, max = 30, message = "'name' {size.message}")
    private String name;

    @NotNull(message = "'description' {notNull.message}", groups = OnCreate.class)
    @NotBlank(message = "'description' {notBlank.message}", groups = OnCreate.class)
    @NullOrNotBlank(message = "'description' {nullOrNotBlank.message}", groups = OnUpdate.class)
    @Size(min = 3, max = 4000, message = "'description' {size.message}")
    private String description;

    @NotNull(message = "'number' {notNull.message}", groups = OnCreate.class)
    @NotBlank(message = "'number' {notBlank.message}", groups = OnCreate.class)
    @NullOrNotBlank(message = "'number' {nullOrNotBlank.message}", groups = OnUpdate.class)
    @Size(min = 3, max = 30, message = "'number' {size.message}")
    private String number;

    @NotNull(message = "'price' {notNull.message}", groups = OnCreate.class)
    @Positive(message = "'price' {positive.message}")
    private Float price;

    @NotNull(message = "'guestsNumber' {notNull.message}", groups = OnCreate.class)
    @Positive(message = "'guestsNumber' {positive.message}")
    private Integer guestsNumber;

    @NotNull(message = "'hotelId' {notNull.message}", groups = OnCreate.class)
    @Positive(message = "'hotelId' {positive.message}")
    private Long hotelId;
}
