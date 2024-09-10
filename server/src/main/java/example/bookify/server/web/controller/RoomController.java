package example.bookify.server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import example.bookify.server.mapper.RoomMapper;
import example.bookify.server.security.annotation.Admin;
import example.bookify.server.security.annotation.User;
import example.bookify.server.service.RoomService;
import example.bookify.server.validator.group.OnCreate;
import example.bookify.server.validator.group.OnUpdate;
import example.bookify.server.web.dto.request.RoomUpsertRequest;
import example.bookify.server.web.dto.request.Pagination;
import example.bookify.server.web.dto.request.filter.RoomFilter;
import example.bookify.server.web.dto.response.ErrorResponse;
import example.bookify.server.web.dto.response.RoomPageResponse;
import example.bookify.server.web.dto.response.RoomResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@User
@RestController
@RequestMapping("/api/rooms")
@Validated
@RequiredArgsConstructor
@Tag(name = "Room", description = "Room API endpoints. Contains all operations related to room.")
@SecurityRequirement(name = "Bearer AuthenticationStat")
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @GetMapping
    @Operation(
            summary = "Get room page",
            description = "Get room page. Returns total elements count, total pages count and list of rooms"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = RoomPageResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            )
    })
    public RoomPageResponse getAll(@Valid Pagination pagination) {
        var roomPage = roomService.findAll(pagination);
        return roomMapper.roomPageToRoomPageResponse(roomPage);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get room by ID",
            description = "Get room. Returns id, name, description, number, price, guests number, hotel id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = RoomResponse.class),
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
    public RoomResponse get(@PathVariable
                            @Positive(message = "'id' {positive.message}")
                            Long id) {

        var room = roomService.findById(id);
        return roomMapper.roomToResponse(room);
    }

    @GetMapping("/filter")
    @Operation(
            summary = "Get room filtered page",
            description = "Get filtered room page. Returns total elements count, total pages count and list of rooms"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = RoomPageResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            )
    })
    public RoomPageResponse filter(@Valid RoomFilter filter) {
        var roomPage = roomService.findByFilter(filter);
        return roomMapper.roomPageToRoomPageResponse(roomPage);
    }

    @Admin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(OnCreate.class)
    @Operation(
            summary = "Create room",
            description = "Create room. Returns id, name, description, number, price, guests number, hotel id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = RoomResponse.class),
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
    public RoomResponse create(@RequestBody @Valid RoomUpsertRequest request) {
        var room = roomService.save(roomMapper.requestToRoom(request));
        return roomMapper.roomToResponse(room);
    }

    @Admin
    @PutMapping("/{id}")
    @Validated(OnUpdate.class)
    @Operation(
            summary = "Update room by ID",
            description = "Update room. Returns id, name, description, number, price, guests number, hotel id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = RoomResponse.class),
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
    public RoomResponse update(@PathVariable
                               @Positive(message = "'id' {positive.message}")
                               Long id,
                               @RequestBody @Valid RoomUpsertRequest request) {

        var room = roomService.update(roomMapper.requestToRoom(id, request));
        return roomMapper.roomToResponse(room);
    }

    @Admin
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete room by ID",
            description = "Delete room. Returns no content"
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

        roomService.deleteById(id);
    }
}
