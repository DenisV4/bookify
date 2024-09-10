package example.bookify.server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import example.bookify.server.mapper.UserMapper;
import example.bookify.server.mapper.UserRoleMapper;
import example.bookify.server.security.annotation.Admin;
import example.bookify.server.security.annotation.AdminOrTargetUser;
import example.bookify.server.security.annotation.User;
import example.bookify.server.service.UserService;
import example.bookify.server.validator.group.OnCreate;
import example.bookify.server.validator.group.OnUpdate;
import example.bookify.server.validator.group.OnValidate;
import example.bookify.server.web.dto.request.Pagination;
import example.bookify.server.web.dto.request.UserUpsertRequest;
import example.bookify.server.web.dto.response.ErrorResponse;
import example.bookify.server.web.dto.response.UserPageResponse;
import example.bookify.server.web.dto.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@User
@RestController
@RequestMapping("/api/users")
@Validated
@RequiredArgsConstructor
@Tag(name = "User", description = "User API endpoints. Contains all operations related to user.")
@SecurityRequirement(name = "Bearer AuthenticationStat")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    @GetMapping
    @Operation(
            summary = "Get user page",
            description = "Get user page. Returns total elements count, total pages count and list of users"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = UserPageResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            )
    })
    public UserPageResponse getAll(@Valid Pagination pagination) {
        var users = userService.findAll(pagination);
        return userMapper.userPageToUserPageResponse(users);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Get user. Returns id, name, email, roles"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = UserResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            )
    })
    public UserResponse get(@PathVariable
                            @Positive(message = "'id' {positive.message}")
                            Long id) {

        var user = userService.findById(id);
        return userMapper.userToResponse(user);
    }

    @Admin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(OnCreate.class)
    @Operation(
            summary = "Create new user",
            description = "Create new user. Returns id, name, email, roles"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = UserResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            )
    })
    public UserResponse create(@RequestBody @Valid UserUpsertRequest request) {
        var roles = userRoleMapper.roleTypeListToRoleList(request.getRoles());
        var user = userService.save(userMapper.requestToUser(request), roles);

        return userMapper.userToResponse(user);
    }

    @AdminOrTargetUser
    @PutMapping("/{id}")
    @Validated(OnUpdate.class)
    @Operation(
            summary = "Update user by ID",
            description = "Update user. Returns id, name, email, roles"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = UserResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            )
    })
    public UserResponse update(@PathVariable
                               @Positive(message = "'id' {positive.message}")
                               Long id,
                               @RequestBody @Valid UserUpsertRequest request) {

        var roles = userRoleMapper.roleTypeListToRoleList(request.getRoles());
        var user = userService.update(userMapper.requestToUser(id, request), roles);
        return userMapper.userToResponse(user);
    }

    @Admin
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete user by ID",
            description = "Delete user. Returns no content"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    content = @Content(mediaType = "/")
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            )
    })
    public void delete(@PathVariable
                       @Positive(message = "'id' {positive.message}")
                       Long id) {

        userService.deleteById(id);
    }

    @GetMapping("/validate/{id}")
    @Validated(OnValidate.class)
    @Operation(
            summary = "Validate user roles",
            description = "Validate user roles. Returns true if list of roles is valid."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(mediaType = "text/plain")}
            )
    })
    public Boolean validateRoles(@PathVariable
                                 @Positive(message = "'id' {positive.message}")
                                 Long id,
                                 @Valid UserUpsertRequest request) {

        var user = userService.findById(id);
        var roles = userRoleMapper.roleTypeListToRoleList(request.getRoles());

        return userService.validateAdminRoles(user, roles);
    }
}
