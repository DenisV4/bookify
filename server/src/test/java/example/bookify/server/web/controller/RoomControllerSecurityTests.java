package example.bookify.server.web.controller;

import example.bookify.server.mapper.RoomMapper;
import example.bookify.server.model.Room;
import example.bookify.server.service.RoomService;
import example.bookify.server.web.dto.request.Pagination;
import example.bookify.server.web.dto.request.filter.RoomFilter;
import example.bookify.server.web.dto.response.RoomPageResponse;
import example.bookify.server.web.dto.response.RoomResponse;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Room Controller Security Tests")
class RoomControllerSecurityTests extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @MockBean
    private RoomMapper roomMapper;

    private final String ROOM_JSON = """
            {
                "name": "Test Room",
                "description": "Test Description",
                "number": "Test Number",
                "price": "100.0",
                "guestsNumber": "2",
                "hotelId": 1
            }
            """;

    private final PageImpl<Room> ROOM_PAGE = new PageImpl<>(List.of(new Room()));

    @Test
    @DisplayName("Should return OK and allow access to GET /api/rooms for USER")
    @WithMockUser(roles = "USER")
    void shouldAllowAccessToGetAllRooms() throws Exception {
        when(roomService.findAll(any(Pagination.class)))
                .thenReturn(ROOM_PAGE);

        when(roomMapper.roomPageToRoomPageResponse(ROOM_PAGE))
                .thenReturn(RoomPageResponse.builder().build());

        mockMvc.perform(get("/api/rooms")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/rooms for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToGetAllRoomsForAdmin() throws Exception {
        when(roomService.findAll(any(Pagination.class)))
                .thenReturn(ROOM_PAGE);

        when(roomMapper.roomPageToRoomPageResponse(ROOM_PAGE))
                .thenReturn(RoomPageResponse.builder().build());

        mockMvc.perform(get("/api/rooms")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to GET /api/rooms for unauthorized users")
    void shouldNotAllowAccessToGetAllRoomsForAnonymous() throws Exception {
        mockMvc.perform(get("/api/rooms")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/rooms/{id} for USER")
    @WithMockUser(roles = "USER")
    void shouldAllowAccessToGetRoomByIdForUser() throws Exception {
        when(roomService.findById(anyLong()))
                .thenReturn(new Room());

        when(roomMapper.roomToResponse(any(Room.class)))
                .thenReturn(RoomResponse.builder().build());

        mockMvc.perform(get("/api/rooms/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/rooms/{id} for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToGetRoomByIdForAdmin() throws Exception {
        when(roomService.findById(anyLong()))
                .thenReturn(new Room());

        when(roomMapper.roomToResponse(any(Room.class)))
                .thenReturn(RoomResponse.builder().build());

        mockMvc.perform(get("/api/rooms/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to GET /api/rooms/{id} for unauthorized users")
    void shouldNotAllowAccessToGetRoomByIdForAnonymous() throws Exception {
        mockMvc.perform(get("/api/rooms/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/rooms/filter for USER")
    @WithMockUser(roles = "USER")
    void shouldAllowAccessToFilterRoomsForUser() throws Exception {
        when(roomService.findByFilter(any(RoomFilter.class)))
                .thenReturn(ROOM_PAGE);

        when(roomMapper.roomPageToRoomPageResponse(ROOM_PAGE))
                .thenReturn(RoomPageResponse.builder().build());

        mockMvc.perform(get("/api/rooms/filter")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return OK and allow access to GET /api/rooms/filter for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToFilterRoomsForAdmin() throws Exception {
        when(roomService.findByFilter(any(RoomFilter.class)))
                .thenReturn(ROOM_PAGE);

        when(roomMapper.roomPageToRoomPageResponse(ROOM_PAGE))
                .thenReturn(RoomPageResponse.builder().build());

        mockMvc.perform(get("/api/rooms/filter")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to GET /api/rooms/filter for unauthorized users")
    void shouldNotAllowAccessToFilterRoomsForAnonymous() throws Exception {
        mockMvc.perform(get("/api/rooms/filter")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return CREATED and allow access to POST /api/rooms for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToCreateRoomForAdmin() throws Exception {
        when(roomService.save(any(Room.class)))
                .thenReturn(new Room());

        when(roomMapper.roomToResponse(any(Room.class)))
                .thenReturn(RoomResponse.builder().build());

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ROOM_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return FORBIDDEN and forbid access to POST /api/rooms for USER")
    @WithMockUser(roles = "USER")
    void shouldNotAllowAccessToCreateRoomForUser() throws Exception {
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ROOM_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to POST /api/rooms for unauthorized users")
    void shouldNotAllowAccessToCreateRoomForAnonymous() throws Exception {
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ROOM_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return CREATED and allow access to PUT /api/rooms for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToUpdateRoomForAdmin() throws Exception {
        when(roomService.update(any(Room.class)))
                .thenReturn(new Room());

        when(roomMapper.roomToResponse(any(Room.class)))
                .thenReturn(RoomResponse.builder().build());

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ROOM_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return FORBIDDEN and forbid access to PUT /api/rooms for USER")
    @WithMockUser(roles = "USER")
    void shouldNotAllowAccessToUpdateRoomForUser() throws Exception {
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ROOM_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to PUT /api/rooms for unauthorized users")
    void shouldNotAllowAccessToUpdateRoomForAnonymous() throws Exception {
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ROOM_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return NOCONTENT and allow access to DELETE /api/rooms for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToDeleteRoomForAdmin() throws Exception {
        doNothing().when(roomService).deleteById(anyLong());

        mockMvc.perform(delete("/api/rooms/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return FORBIDDEN and forbid access to DELETE /api/rooms for USER")
    @WithMockUser(roles = "USER")
    void shouldNotAllowAccessToDeleteRoomForUser() throws Exception {
        mockMvc.perform(delete("/api/rooms/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED and forbid access to DELETE /api/rooms for unauthorized users")
    void shouldNotAllowAccessToDeleteRoomForAnonymous() throws Exception {
        mockMvc.perform(delete("/api/rooms/1"))
                .andExpect(status().isUnauthorized());
    }
}
