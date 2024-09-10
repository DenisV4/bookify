package example.bookify.server.statistics.web;

import example.bookify.server.statistics.mapper.AuthenticationStatMapper;
import example.bookify.server.statistics.mapper.BookingStatMapper;
import example.bookify.server.statistics.mapper.UserRegistrationMapper;
import example.bookify.server.statistics.service.AuthenticationStatService;
import example.bookify.server.statistics.service.BookingStatService;
import example.bookify.server.statistics.service.FileService;
import example.bookify.server.statistics.service.UserRegistrationService;
import example.bookify.server.web.controller.AbstractControllerTest;
import example.bookify.server.web.dto.request.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Statistics Controller Security Tests")
class StatisticsControllerSecurityTests extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationStatService authenticationStatService;

    @MockBean
    private UserRegistrationService userRegistrationService;

    @MockBean
    private BookingStatService bookingStatService;

    @MockBean
    private FileService fileService;

    @MockBean
    private AuthenticationStatMapper authenticationStatMapper;

    @MockBean
    private UserRegistrationMapper userRegistrationMapper;

    @MockBean
    private BookingStatMapper bookingStatMapper;

    @Test
    @DisplayName("Should return OK and allow access to GET /api/statistics/logins for USER")
    @WithMockUser(roles = "USER")
    void shouldAllowAccessToGetLoginsForUser() throws Exception {
        when(authenticationStatService.findAll(any(Pagination.class)))
                .thenReturn(Mono.empty());

        mockMvc.perform(get("/api/statistics/logins?pageSize=10&pageNumber=0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/statistics/logins for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToGetLoginsForAdmin() throws Exception {
        when(authenticationStatService.findAll(any(Pagination.class)))
                .thenReturn(Mono.empty());

        mockMvc.perform(get("/api/statistics/logins?pageSize=10&pageNumber=0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to GET /api/statistics/logins for unauthorized users")
    void shouldForbidAccessToGetLoginsForUnauthorized() throws Exception {
        mockMvc.perform(get("/api/statistics/logins?pageSize=10&pageNumber=0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return OK and allow access to  GET /api/statistics/registrations for USER")
    @WithMockUser(roles = "USER")
    void shouldAllowAccessToGetRegistrationsForUser() throws Exception {
        when(userRegistrationService.findAll(any(Pagination.class)))
                .thenReturn(Mono.empty());

        mockMvc.perform(get("/api/statistics/registrations?pageSize=10&pageNumber=0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/statistics/registrations for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToGetRegistrationsForAdmin() throws Exception {
        when(userRegistrationService.findAll(any(Pagination.class)))
                .thenReturn(Mono.empty());

        mockMvc.perform(get("/api/statistics/registrations?pageSize=10&pageNumber=0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to GET /api/statistics/registrations for unauthorized users")
    void shouldForbidAccessToGetRegistrationsForUnauthorized() throws Exception {
        mockMvc.perform(get("/api/statistics/registrations?pageSize=10&pageNumber=0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/statistics/bookings for USER")
    @WithMockUser(roles = "USER")
    void shouldAllowAccessToGetBookingsForUser() throws Exception {
        when(bookingStatService.findAll(any(Pagination.class)))
                .thenReturn(Mono.empty());

        mockMvc.perform(get("/api/statistics/bookings?pageSize=10&pageNumber=0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/statistics/bookings for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToGetBookingsForAdmin() throws Exception {
        when(bookingStatService.findAll(any(Pagination.class)))
                .thenReturn(Mono.empty());

        mockMvc.perform(get("/api/statistics/bookings?pageSize=10&pageNumber=0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to GET /api/statistics/bookings for unauthorized users")
    void shouldForbidAccessToGetBookingsForUnauthorized() throws Exception {
        mockMvc.perform(get("/api/statistics/bookings?pageSize=10&pageNumber=0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/statistics/download for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToGetDownloadForAdmin() throws Exception {
        when(fileService.generateStatisticsFile())
                .thenReturn(Mono.empty());

        mockMvc.perform(get("/api/statistics/download"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return FORBIDDEN and forbid access to GET /api/statistics/download for USER")
    @WithMockUser(roles = "USER")
    void shouldForbidAccessToGetDownloadForUser() throws Exception {
        mockMvc.perform(get("/api/statistics/download"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to GET /api/statistics/download for unauthorized users")
    void shouldForbidAccessToGetDownloadForUnauthorized() throws Exception {
        mockMvc.perform(get("/api/statistics/download"))
                .andExpect(status().isUnauthorized());
    }
}
