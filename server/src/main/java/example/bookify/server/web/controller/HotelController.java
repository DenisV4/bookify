package example.bookify.server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import example.bookify.server.mapper.HotelMapper;
import example.bookify.server.security.annotation.Admin;
import example.bookify.server.security.annotation.User;
import example.bookify.server.service.HotelRatingService;
import example.bookify.server.service.HotelService;
import example.bookify.server.validator.group.OnCreate;
import example.bookify.server.validator.group.OnUpdate;
import example.bookify.server.web.dto.request.HotelRateRequest;
import example.bookify.server.web.dto.request.HotelUpsertRequest;
import example.bookify.server.web.dto.request.Pagination;
import example.bookify.server.web.dto.request.filter.HotelFilter;
import example.bookify.server.web.dto.response.ErrorResponse;
import example.bookify.server.web.dto.response.HotelResponse;
import example.bookify.server.web.dto.response.HotelPageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@User
@RestController
@RequestMapping("/api/hotels")
@Validated
@RequiredArgsConstructor
@Tag(name = "Hotel", description = "Hotel API endpoints. Contains all operations related to hotel.")
@SecurityRequirement(name = "Bearer AuthenticationStat")
public class HotelController {

    private final HotelService hotelService;
    private final HotelMapper hotelMapper;
    private final HotelRatingService hotelRatingService;

    @GetMapping
    @Operation(
            summary = "Get hotel page",
            description = "Get hotel page. Returns total elements count, total pages count and list of hotels"
    )
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = HotelPageResponse.class),
                                            mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorResponse.class),
                                            mediaType = "application/json")
                            }
                    )
            }
    )
    public HotelPageResponse getAll(@Valid Pagination pagination) {
        var hotelPage = hotelService.findAll(pagination);
        return hotelMapper.hotelPageToHotelPageResponse(hotelPage);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get hotel by ID",
            description = "Get hotel. Returns id, name, title, city, address, distance, rating, ratings count"
    )
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = HotelResponse.class),
                                            mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorResponse.class),
                                            mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorResponse.class),
                                            mediaType = "application/json")
                            }
                    )
            }
    )
    public HotelResponse get(@PathVariable
                             @Positive(message = "'id' {positive.message}")
                             Long id) {

        var hotel = hotelService.findById(id);
        return hotelMapper.hotelToResponse(hotel);
    }

    @GetMapping("/filter")
    @Operation(
            summary = "Get filtered hotel page",
            description = "Get filtered hotel page. Returns total elements count, total pages count and list of hotels"
    )
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = HotelPageResponse.class),
                                            mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorResponse.class),
                                            mediaType = "application/json")
                            }
                    )
            }
    )
    public HotelPageResponse filter(@Valid HotelFilter filter) {
        var hotelPage = hotelService.findByFilter(filter);
        return hotelMapper.hotelPageToHotelPageResponse(hotelPage);
    }

    @Admin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(OnCreate.class)
    @Operation(
            summary = "Create new hotel",
            description = "Create new hotel. Returns id, name, title, city, address, distance, rating, ratings count"
    )
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "201",
                            content = {
                                    @Content(schema = @Schema(implementation = HotelResponse.class),
                                            mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorResponse.class),
                                            mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorResponse.class),
                                            mediaType = "application/json")
                            }
                    )
            }
    )
    public HotelResponse create(@RequestBody @Valid HotelUpsertRequest request) {
        var hotel = hotelService.save(hotelMapper.requestToHotel(request));
        return hotelMapper.hotelToResponse(hotel);
    }

    @Admin
    @PutMapping("/{id}")
    @Validated(OnUpdate.class)
    @Operation(
            summary = "Update hotel by ID",
            description = "Update hotel. Returns id, name, title, city, address, distance, rating, ratings count"
    )
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = HotelResponse.class),
                                            mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorResponse.class),
                                            mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorResponse.class),
                                            mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorResponse.class),
                                            mediaType = "application/json")
                            }
                    )
            }
    )
    public HotelResponse update(@PathVariable
                                @Positive(message = "'id' {positive.message}")
                                Long id,
                                @RequestBody @Valid HotelUpsertRequest request) {

        var hotel = hotelService.update(hotelMapper.requestToHotel(id, request));
        return hotelMapper.hotelToResponse(hotel);
    }

    @Admin
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete hotel by ID",
            description = "Delete hotel. Returns no content"
    )
    @ApiResponses(
            {
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
            }
    )
    public void delete(@PathVariable
                       @Positive(message = "'id' {positive.message}")
                       Long id) {

        hotelService.deleteById(id);
    }

    @PutMapping("/rate/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Rate hotel by ID",
            description = "Rate hotel. Returns no content"
    )
    @ApiResponses(
            {
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
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json")
                    )
            }
    )
    public void rate(@PathVariable
                     @Positive(message = "'id' {positive.message}")
                     Long id,
                     @RequestBody @Valid HotelRateRequest request) {

        hotelRatingService.rate(id, request.getScore());
    }
}
