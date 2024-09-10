package example.bookify.server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import example.bookify.server.mapper.BookingMapper;
import example.bookify.server.security.annotation.Admin;
import example.bookify.server.security.annotation.User;
import example.bookify.server.service.BookingService;
import example.bookify.server.web.dto.request.BookingRequest;
import example.bookify.server.web.dto.request.Pagination;
import example.bookify.server.web.dto.response.BookingPageResponse;
import example.bookify.server.web.dto.response.BookingResponse;
import example.bookify.server.web.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@User
@RestController
@RequestMapping("/api/bookings")
@Validated
@RequiredArgsConstructor
@Tag(name = "Booking", description = "Booking API endpoints. Contains all operations related to booking.")
@SecurityRequirement(name = "Bearer AuthenticationStat")
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @Admin
    @GetMapping
    @Operation(
            summary = "Get booking page",
            description = "Get booking page. Returns total elements count, total pages count and list of bookings."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BookingPageResponse.class),
                            mediaType = "application/json")
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
    public BookingPageResponse getAll(@Valid Pagination pagination) {
        var bookings = bookingService.findAll(pagination);
        return bookingMapper.pageToBookingPageResponse(bookings);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create booking",
            description = "Create booking. Returns room id, user id, check in date, check out date."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class),
                            mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "409",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")
            )
    })
    public BookingResponse book(@RequestBody @Valid BookingRequest request) {
        var booking = bookingService.book(bookingMapper.requestToBooking(request));

        return bookingMapper.bookingToResponse(booking);
    }
}
