package example.bookify.server.web.controller;

import example.bookify.server.web.dto.response.RoomPageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@Sql(
        scripts = {"classpath:db/init.sql", "classpath:db/room-controller-filter-test-init.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@DisplayName("RoomController Integration Tests")
class RoomControllerIntegrationTests extends AbstractWebControllerTest {

    @Test
    @DisplayName("Should return available room when no bookings overlap with provided dates")
    void shouldReturnRoomPageWithAvailableRoomWhenNoBookingsOverlap() throws Exception {
        var response = mockMvc.perform(get("/api/rooms/filter")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("checkInDate", "2024-09-15")
                        .param("checkOutDate", "2024-09-20"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var roomPageResponse = (RoomPageResponse) parseResponse(RoomPageResponse.class, response);
        assertThat(roomPageResponse.getRooms()).hasSize(1);
        assertThat(roomPageResponse.getRooms().get(0).getName()).isEqualTo("testRoom");
    }

    @Test
    @DisplayName("Should return room page with no rooms when there is a full overlap with an existing booking")
    void shouldReturnRoomPageWithNoRoomsWhenFullOverlapWithExistingBooking() throws Exception {
        String response = mockMvc.perform(get("/api/rooms/filter")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("checkInDate", "2024-09-09")
                        .param("checkOutDate", "2024-09-16"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var roomPageResponse = (RoomPageResponse) parseResponse(RoomPageResponse.class, response);
        assertThat(roomPageResponse.getRooms()).isEmpty();
    }

    @Test
    @DisplayName("Should return room page with no rooms when there is a partial overlap at the start")
    void shouldReturnRoomPageWithNoRoomsWhenPartialOverlapAtStart() throws Exception {
        String response = mockMvc.perform(get("/api/rooms/filter")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("checkInDate", "2024-09-14")
                        .param("checkOutDate", "2024-09-20"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var roomPageResponse = (RoomPageResponse) parseResponse(RoomPageResponse.class, response);
        assertThat(roomPageResponse.getRooms()).isEmpty();
    }

    @Test
    @DisplayName("Should return room page with no rooms when there is a partial overlap at the end")
    void shouldReturnRoomPageWithNoRoomsWhenPartialOverlapAtEnd() throws Exception {
        String response = mockMvc.perform(get("/api/rooms/filter")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("checkInDate", "2024-09-15")
                        .param("checkOutDate", "2024-09-21"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var roomPageResponse = (RoomPageResponse) parseResponse(RoomPageResponse.class, response);
        assertThat(roomPageResponse.getRooms()).isEmpty();
    }
}
