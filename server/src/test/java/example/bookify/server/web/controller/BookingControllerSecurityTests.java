package example.bookify.server.web.controller;

import example.bookify.server.mapper.BookingMapper;
import example.bookify.server.model.Booking;
import example.bookify.server.service.BookingService;
import example.bookify.server.web.dto.request.Pagination;
import example.bookify.server.web.dto.response.BookingPageResponse;
import example.bookify.server.web.dto.response.BookingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("BookingController Security Tests")
class BookingControllerSecurityTests extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private BookingMapper bookingMapper;

    private final String BOOKING_JSON = """ 
                {
                    "userId": "1",
                    "roomId": "1",
                    "checkInDate": "2024-09-10",
                    "checkOutDate": "2024-09-20"
                }
            """;

    private final Page<Booking> BOOKING_PAGE = new PageImpl<>(List.of(new Booking()));

    @Test
    @DisplayName("Should return OK and allow access to GET /api/bookings for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToGetAllBookingsForAdmin() throws Exception {
        when(bookingService.findAll(any(Pagination.class)))
                .thenReturn(BOOKING_PAGE);

        when(bookingMapper.pageToBookingPageResponse(BOOKING_PAGE))
                .thenReturn(BookingPageResponse.builder().build());

        mockMvc.perform(get("/api/bookings")
                        .param("pageSize", "10")
                        .param("pageNumber", "0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return FORBIDDEN and forbid access to GET /api/bookings for USER")
    @WithMockUser(roles = "USER")
    void shouldNotAllowAccessToGetAllBookingsForUser() throws Exception {
        mockMvc.perform(get("/api/bookings")
                        .param("pageSize", "10")
                        .param("pageNumber", "0"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to GET /api/bookings for unauthorized users")
    void shouldNotAllowAccessToGetAllBookingsForAnonymous() throws Exception {
        mockMvc.perform(get("/api/bookings")
                        .param("pageSize", "10")
                        .param("pageNumber", "0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return CREATED and allow access to POST /api/bookings for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToBookRoomForAdmin() throws Exception {
        when(bookingService.book(any(Booking.class)))
                .thenReturn(new Booking());

        when(bookingMapper.bookingToResponse(any(Booking.class)))
                .thenReturn(BookingResponse.builder().build());

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BOOKING_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return CREATED and allow access to POST /api/bookings for USER")
    @WithMockUser(roles = "USER")
    void shouldAllowAccessToBookRoomForUser() throws Exception {
        when(bookingService.book(any(Booking.class)))
                .thenReturn(new Booking());

        when(bookingMapper.bookingToResponse(any(Booking.class)))
                .thenReturn(BookingResponse.builder().build());

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BOOKING_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to POST /api/bookings for unauthorized users")
    void shouldNotAllowAccessToBookRoomForAnonymous() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BOOKING_JSON))
                .andExpect(status().isUnauthorized());
    }
}
