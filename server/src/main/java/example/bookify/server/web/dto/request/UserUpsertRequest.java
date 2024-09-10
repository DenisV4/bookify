package example.bookify.server.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import example.bookify.server.model.UserRole;
import example.bookify.server.validator.annotation.NullOrNotBlank;
import example.bookify.server.validator.annotation.NullOrNotEmpty;
import example.bookify.server.validator.group.OnCreate;
import example.bookify.server.validator.group.OnRegister;
import example.bookify.server.validator.group.OnUpdate;
import example.bookify.server.validator.group.OnValidate;

import java.util.List;

@Data
@Schema(
        title = "User request",
        description = "Insert, update or register user"
)
public class UserUpsertRequest {

    @NotNull(message = "'name' {notNull.message}", groups = {OnCreate.class, OnRegister.class})
    @NotBlank(message = "'name' {notBlank.message}", groups = {OnCreate.class, OnRegister.class})
    @NullOrNotBlank(message = "'name' {nullOrNotBlank.message}", groups = OnUpdate.class)
    @Size(min = 3, max = 30, message = "'name' {size.message}")
    String name;

    @NotNull(message = "'email' {notNull.message}", groups = {OnCreate.class, OnRegister.class})
    @Email(message = "'email' {email.message}", groups = {OnCreate.class, OnRegister.class})
    String email;

    @NotNull(message = "'password' {notNull.message}", groups = {OnCreate.class, OnRegister.class})
    @NotBlank(message = "'password' {notBlank.message}", groups = {OnCreate.class, OnRegister.class})
    @NullOrNotBlank(message = "'password' {nullOrNotBlank.message}", groups = OnUpdate.class)
    @Size(min = 3, max = 30, message = "'password' {size.message}")
    String password;

    @NotNull(message = "'roles' {notNull.message}", groups = {OnCreate.class, OnValidate.class})
    @NotEmpty(message = "'roles' {notEmpty.message}", groups = {OnCreate.class, OnValidate.class})
    @NullOrNotEmpty(message = "'roles' {nullOrNotEmpty.message}", groups = OnUpdate.class)
    List<UserRole.RoleType> roles;
}
