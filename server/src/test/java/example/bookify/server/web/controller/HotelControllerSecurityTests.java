package example.bookify.server.web.controller;

import example.bookify.server.model.Hotel;
import example.bookify.server.service.HotelRatingService;
import example.bookify.server.service.HotelService;
import example.bookify.server.web.dto.request.Pagination;
import example.bookify.server.web.dto.request.filter.HotelFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("unchecked")
@DisplayName("HotelController Security Tests")
class HotelControllerSecurityTests extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

    @MockBean
    private HotelRatingService hotelRatingService;

    private final String HOTEL_JSON = """
            {
                "name": "Test Hotel",
                "title": "Test Title",
                "city": "Test City",
                "address": "Test Address",
                "distance": "1.0"
            }
            """;

    private final String HOTEL_RATING_JSON = """
            {
                "score": 5
            }
            """;

    @Test
    @DisplayName("Should return OK and allow access to GET /api/hotels for USER")
    @WithMockUser(roles = "USER")
    void shouldAllowAccessToGetAllHotelsForUser() throws Exception {
        when(hotelService.findAll(any(Pagination.class)))
                .thenReturn(any(PageImpl.class));

        mockMvc.perform(get("/api/hotels")
                        .param("pageSize", "10")
                        .param("pageNumber", "0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/hotels for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToGetAllHotelsForAdmin() throws Exception {
        when(hotelService.findAll(any(Pagination.class)))
                .thenReturn(any(PageImpl.class));

        mockMvc.perform(get("/api/hotels")
                        .param("pageSize", "10")
                        .param("pageNumber", "0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to GET /api/hotels for unauthorized users")
    void shouldNotAllowAccessToGetAllHotelsForAnonymous() throws Exception {
        mockMvc.perform(get("/api/hotels")
                        .param("pageSize", "10")
                        .param("pageNumber", "0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/hotels/{id} for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToGetHotelByIdForAdmin() throws Exception {
        when(hotelService.findById(anyLong()))
                .thenReturn(Hotel.builder().build());

        mockMvc.perform(get("/api/hotels/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/hotels/{id} for USER")
    @WithMockUser(roles = "USER")
    void shouldAllowAccessToGetHotelByIdForUser() throws Exception {
        when(hotelService.findById(anyLong()))
                .thenReturn(Hotel.builder().build());

        mockMvc.perform(get("/api/hotels/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to GET /api/hotels/{id} for unauthorized users")
    void shouldNotAllowAccessToGetHotelByIdForAnonymous() throws Exception {
        mockMvc.perform(get("/api/hotels/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/hotels/filter for USER")
    @WithMockUser(roles = "USER")
    void shouldAllowAccessToHotelsFilterForUser() throws Exception {
        when(hotelService.findByFilter(any(HotelFilter.class)))
                .thenReturn(any(PageImpl.class));

        mockMvc.perform(get("/api/hotels/filter")
                        .param("pageSize", "10")
                        .param("pageNumber", "0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/hotels/filter for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToHotelsFilterForAdmin() throws Exception {
        when(hotelService.findByFilter(any(HotelFilter.class)))
                .thenReturn(any(PageImpl.class));

        mockMvc.perform(get("/api/hotels/filter")
                        .param("pageSize", "10")
                        .param("pageNumber", "0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to GET /api/hotels/filter for unauthorized users")
    void shouldNotAllowAccessToHotelsFilterForAnonymous() throws Exception {
        mockMvc.perform(get("/api/hotels/filter")
                        .param("pageSize", "10")
                        .param("pageNumber", "0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return CREATED and allow access to POST /api/hotels for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToCreateHotelForAdmin() throws Exception {
        when(hotelService.save(any(Hotel.class)))
                .thenReturn(new Hotel());

        mockMvc.perform(post("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(HOTEL_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return FORBIDDEN and forbid access to POST /api/hotels for USER")
    @WithMockUser(roles = "USER")
    void shouldNotAllowAccessToCreateHotelForUser() throws Exception {
        mockMvc.perform(post("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(HOTEL_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to POST /api/hotels for unauthorized users")
    void shouldNotAllowAccessToCreateHotelForAnonymous() throws Exception {
        mockMvc.perform(post("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(HOTEL_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return OK and allow access to PUT /api/hotels for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToUpdateHotelForAdmin() throws Exception {
        when(hotelService.update(any(Hotel.class)))
                .thenReturn(new Hotel());

        mockMvc.perform(put("/api/hotels/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(HOTEL_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return FORBIDDEN and forbid access to PUT /api/hotels for USER")
    @WithMockUser(roles = "USER")
    void shouldNotAllowAccessToUpdateHotelForUser() throws Exception {
        mockMvc.perform(put("/api/hotels/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(HOTEL_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to PUT /api/hotels for unauthorized users")
    void shouldNotAllowAccessToUpdateHotelForAnonymous() throws Exception {
        mockMvc.perform(put("/api/hotels/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(HOTEL_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return NOCONTENT and allow access to DELETE /api/hotels for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToDeleteHotelForAdmin() throws Exception {
        doNothing().when(hotelService).deleteById(anyLong());

        mockMvc.perform(delete("/api/hotels/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return FORBIDDEN and forbid access to DELETE /api/hotels for USER")
    @WithMockUser(roles = "USER")
    void shouldNotAllowAccessToDeleteHotelForUser() throws Exception {
        mockMvc.perform(delete("/api/hotels/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to DELETE /api/hotels for unauthorized users")
    void shouldNotAllowAccessToDeleteHotelForAnonymous() throws Exception {
        mockMvc.perform(delete("/api/hotels/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return NOCONTENT and allow access to PUT /api/hotels/rate for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToRateHotelForAdmin() throws Exception {
        doNothing().when(hotelRatingService).rate(anyLong(), anyInt());

        mockMvc.perform(put("/api/hotels/rate/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(HOTEL_RATING_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return NOCONTENT and allow access to PUT /api/hotels/rate for USER")
    @WithMockUser(roles = "USER")
    void shouldAllowAccessToRateHotelForUser() throws Exception {
        doNothing().when(hotelRatingService).rate(anyLong(), anyInt());

        mockMvc.perform(put("/api/hotels/rate/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(HOTEL_RATING_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to PUT /api/hotels/rate for unauthorized users")
    void shouldNotAllowAccessToRateHotelForAnonymous() throws Exception {
        mockMvc.perform(put("/api/hotels/rate/1"))
                .andExpect(status().isUnauthorized());
    }
}
