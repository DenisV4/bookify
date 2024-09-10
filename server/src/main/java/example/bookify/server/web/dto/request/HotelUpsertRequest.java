package example.bookify.server.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import example.bookify.server.validator.annotation.NullOrNotBlank;
import example.bookify.server.validator.group.OnCreate;
import example.bookify.server.validator.group.OnUpdate;

@Data
@FieldNameConstants
@Schema(
        title = "Hotel request",
        description = "Insert or update hotel"
)
public class HotelUpsertRequest {

    @NotNull(message = "'name' {notNull.message}", groups = OnCreate.class)
    @NotBlank(message = "'name' {notBlank.message}", groups = OnCreate.class)
    @NullOrNotBlank(message = "'name' {nullOrNotBlank.message}", groups = OnUpdate.class)
    @Size(min = 3, max = 30, message = "'name' {size.message}")
    private String name;

    @NotNull(message = "'title' {notNull.message}", groups = OnCreate.class)
    @NotBlank(message = "'title' {notBlank.message}", groups = OnCreate.class)
    @NullOrNotBlank(message = "'title' {nullOrNotBlank.message}", groups = OnUpdate.class)
    @Size(min = 3, max = 300, message = "'title' {size.message}")
    private String title;

    @NotNull(message = "'city' {notNull.message}", groups = OnCreate.class)
    @NotBlank(message = "'city' {notBlank.message}", groups = OnCreate.class)
    @NullOrNotBlank(message = "'city' {nullOrNotBlank.message}", groups = OnUpdate.class)
    @Size(min = 3, max = 30, message = "'city' {size.message}")
    private String city;

    @NotNull(message = "'address' {notNull.message}", groups = OnCreate.class)
    @NotBlank(message = "'address' {notBlank.message}", groups = OnCreate.class)
    @NullOrNotBlank(message = "'address' {nullOrNotBlank.message}", groups = OnUpdate.class)
    @Size(min = 3, max = 100, message = "'address' {size.message}")
    private String address;

    @NotNull(message = "'distance' {notNull.message}", groups = OnCreate.class)
    @Positive(message = "'distance' {positive.message}")
    private Float distance;
}
