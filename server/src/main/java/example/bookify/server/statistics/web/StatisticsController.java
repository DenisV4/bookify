package example.bookify.server.statistics.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import example.bookify.server.security.annotation.Admin;
import example.bookify.server.security.annotation.User;
import example.bookify.server.statistics.mapper.AuthenticationStatMapper;
import example.bookify.server.statistics.mapper.BookingStatMapper;
import example.bookify.server.statistics.mapper.UserRegistrationMapper;
import example.bookify.server.statistics.service.AuthenticationStatService;
import example.bookify.server.statistics.service.BookingStatService;
import example.bookify.server.statistics.service.FileService;
import example.bookify.server.statistics.service.UserRegistrationService;
import example.bookify.server.statistics.web.dto.AuthenticationStatPageResponse;
import example.bookify.server.statistics.web.dto.BookingStatPageResponse;
import example.bookify.server.statistics.web.dto.UserRegistrationPageResponse;
import example.bookify.server.web.dto.request.Pagination;
import example.bookify.server.web.dto.response.ErrorResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@User
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Validated
@Tag(name = "Statistics", description = "Statistics API endpoints. Contains all operations related to statistics.")
@SecurityRequirement(name = "Bearer AuthenticationStat")
public class StatisticsController {

    private final AuthenticationStatService authenticationStatService;
    private final UserRegistrationService userRegistrationService;
    private final BookingStatService bookingStatService;
    private final FileService fileService;

    private final AuthenticationStatMapper authenticationStatMapper;
    private final UserRegistrationMapper userRegistrationMapper;
    private final BookingStatMapper bookingStatMapper;

    @GetMapping("/logins")
    @Operation(
            summary = "Get authentication statistics page",
            description = "Get authentication statistics page. Returns total elements count, total pages count and list of authentications."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = AuthenticationStatPageResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            ),
    })
    public Mono<AuthenticationStatPageResponse> getAuthentications(@Valid Pagination pagination) {
        return authenticationStatService.findAll(pagination)
                .map(authenticationStatMapper::authenticationPageToAuthenticationStatPageResponse);
    }

    @GetMapping("/registrations")
    @Operation(
            summary = "Get user registration statistics page",
            description = "Get user registration statistics page. Returns total elements count, total pages count and list of user registrations."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = UserRegistrationPageResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            ),
    })
    public Mono<UserRegistrationPageResponse> getUserRegistrations(@Valid Pagination pagination) {
        return userRegistrationService.findAll(pagination)
                .map(userRegistrationMapper::userRegistrationPageToUserRegistrationPageResponse);
    }
    @GetMapping("/bookings")
    @Operation(
            summary = "Get booking statistics page",
            description = "Get booking statistics page. Returns total elements count, total pages count and list of bookings."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = BookingStatPageResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            )
    })
    public Mono<BookingStatPageResponse> getBookings(@Valid Pagination pagination) {
        return bookingStatService.findAll(pagination)
                .map(bookingStatMapper::bookingPageToBookingStatPageResponse);
    }

    @Admin
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(
            summary = "Download statistics file",
            description = "Download statistics file. Returns file in zip format with all statistics csv files."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(mediaType = "application/octet-stream")}
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
    public Mono<ResponseEntity<Resource>> download() {
        return fileService.generateStatisticsFile()
                .map(res -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"statistics.zip\"")
                        .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                        .body(new ByteArrayResource(res.toByteArray())));
    }
}
