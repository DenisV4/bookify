package example.bookify.server.service.impl;

import example.bookify.server.model.Room;
import example.bookify.server.repository.RoomRepository;
import example.bookify.server.service.RoomService;
import example.bookify.server.web.dto.request.Pagination;
import example.bookify.server.web.dto.request.filter.RoomFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("RoomService Cache Tests")
@ContextConfiguration(classes = {
        RoomService.class,
        RoomServiceImpl.class
})
@SuppressWarnings("unchecked")
class RoomServiceImplCacheTests extends AbstractServiceCacheTest {
    @Autowired
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepository;

    @Test
    @DisplayName("Should cache the results of the findAll method")
    void shouldCacheResultsOfFindAllMethod() {
        var pagination = new Pagination();
        pagination.setPageNumber(0);
        pagination.setPageSize(10);

        var room1 = Room.builder().id(1L).build();
        var room2 = Room.builder().id(2L).build();
        var page = createPage(0, 10, List.of(room1, room2));

        when(roomRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(page);

        assertCacheKeyCount(0);

        var result1 = roomService.findAll(pagination);
        var result2 = roomService.findAll(pagination);

        assertCacheContainsKey("rooms::Pagination(pageNumber=0, pageSize=10)");
        assertPagesEqual(result1, result2);

        verify(roomRepository, times(1))
                .findAll(ArgumentMatchers.any(PageRequest.class));
    }

    @Test
    @DisplayName("Should cache the results of findByFilter method")
    void shouldCacheResultsOfFindByFilterMethod() {
        var filter = new RoomFilter();
        filter.setPageNumber(0);
        filter.setPageSize(10);

        var room1 = Room.builder().id(1L).build();
        var room2 = Room.builder().id(2L).build();
        var page = createPage(0, 10, List.of(room1, room2));

        when(roomRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(page);

        assertCacheKeyCount(0);

        var result1 = roomService.findByFilter(filter);
        var result2 = roomService.findByFilter(filter);

        assertCacheContainsKey("rooms::RoomFilter(pageNumber=0, pageSize=10)");
        assertPagesEqual(result1, result2);

        verify(roomRepository, times(1))
                .findAll(any(Specification.class), any(PageRequest.class));
    }

    @Test
    @DisplayName("Should cache the result of findById method")
    void shouldCacheResultOfFindByIdMethod() {
        var room = Room.builder().id(1L).build();

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        assertCacheKeyCount(0);

        var result1 = roomService.findById(1L);
        var result2 = roomService.findById(1L);

        assertCacheContainsKey("room::1");
        assertObjectsEqual(result1, result2);

        verify(roomRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should evict 'rooms' cache when saving a new Room")
    void shouldUpdateCacheWhenSavingNewRoom() {
        var room = Room.builder().build();
        var existingRooms = createPage(0, 10, List.of(room));

        when(roomRepository.save(room)).thenReturn(room);

        assertCacheKeyCount(0);

        populateCache("rooms::Pagination(pageNumber=0, pageSize=10)", existingRooms);

        assertCacheKeyCount(1);

        roomService.save(room);

        assertCacheDoesNotContainKey("rooms::*");
    }

    @Test
    @DisplayName("Should update 'room' cache and evict 'rooms' cache when updating an existing Room")
    void shouldUpdateCacheWhenUpdatingExistingRoom() {
        var room = Room.builder().id(1L).name("Updated Room").build();
        var existingRoom = Room.builder().id(1L).name("Existing Room").build();
        var existingRooms = createPage(0, 10, List.of(existingRoom));

        when(roomRepository.findById(1L)).thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(room)).thenReturn(room);

        assertCacheKeyCount(0);

        populateCache("rooms::Pagination(pageNumber=0, pageSize=10)", existingRooms);
        populateCache("room::1", existingRoom);

        assertCacheKeyCount(2);

        roomService.update(room);

        var cachedRoom = getCacheValue("room::1");
        assertObjectsEqual(cachedRoom, room);
        assertCacheDoesNotContainKey("rooms::*");
    }

    @Test
    @DisplayName("Should evict cache when deleting a Room")
    void shouldEvictCacheWhenDeletingRoom() {
        var roomId = 1L;
        var existingRoom = Room.builder().id(1L).name("Existing Room").build();
        var existingRooms = createPage(0, 10, List.of(existingRoom));

        doNothing().when(roomRepository).deleteById(roomId);

        assertCacheKeyCount(0);

        populateCache("rooms::Pagination(pageNumber=0, pageSize=10)", existingRooms);
        populateCache("room::1", existingRoom);

        assertCacheKeyCount(2);

        roomService.deleteById(roomId);

        assertCacheDoesNotContainKey("room::1");
        assertCacheDoesNotContainKey("rooms::*");
    }
}
