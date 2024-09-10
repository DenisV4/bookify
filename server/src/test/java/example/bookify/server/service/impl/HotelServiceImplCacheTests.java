package example.bookify.server.service.impl;

import example.bookify.server.model.Hotel;
import example.bookify.server.repository.HotelRepository;
import example.bookify.server.service.HotelService;
import example.bookify.server.web.dto.request.Pagination;
import example.bookify.server.web.dto.request.filter.HotelFilter;
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

@DisplayName("HotelService Cache Tests")
@ContextConfiguration(classes = {
        HotelService.class,
        HotelServiceImpl.class
})
@SuppressWarnings("unchecked")
class HotelServiceImplCacheTests extends AbstractServiceCacheTest {

    @Autowired
    private HotelService hotelService;

    @MockBean
    private HotelRepository hotelRepository;

    @Test
    @DisplayName("Should cache the results of the findAll method")
    void shouldCacheResultsOfFindAllMethod() {
        var pagination = new Pagination();
        pagination.setPageNumber(0);
        pagination.setPageSize(10);

        var hotel1 = Hotel.builder().id(1L).build();
        var hotel2 = Hotel.builder().id(2L).build();
        var page = createPage(0, 10, List.of(hotel1, hotel2));

        when(hotelRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(page);

        assertCacheKeyCount(0);

        var result1 = hotelService.findAll(pagination);
        var result2 = hotelService.findAll(pagination);

        assertCacheContainsKey("hotels::Pagination(pageNumber=0, pageSize=10)");
        assertPagesEqual(result1, result2);

        verify(hotelRepository, times(1))
                .findAll(ArgumentMatchers.any(PageRequest.class));
    }

    @Test
    @DisplayName("Should cache the results of findByFilter method")
    void shouldCacheResultsOfFindByFilterMethod() {
        var filter = new HotelFilter();
        filter.setPageNumber(0);
        filter.setPageSize(10);

        var hotel1 = Hotel.builder().id(1L).build();
        var hotel2 = Hotel.builder().id(2L).build();
        var page = createPage(0, 10, List.of(hotel1, hotel2));

        when(hotelRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(page);

        assertCacheKeyCount(0);

        var result1 = hotelService.findByFilter(filter);
        var result2 = hotelService.findByFilter(filter);

        assertCacheContainsKey("hotels::HotelFilter(pageNumber=0, pageSize=10)");
        assertPagesEqual(result1, result2);

        verify(hotelRepository, times(1))
                .findAll(any(Specification.class), any(PageRequest.class));
    }

    @Test
    @DisplayName("Should cache the result of findById method")
    void shouldCacheResultOfFindByIdMethod() {
        var hotel = Hotel.builder().id(1L).build();

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        assertCacheKeyCount(0);

        var result1 = hotelService.findById(1L);
        var result2 = hotelService.findById(1L);

        assertCacheContainsKey("hotel::1");

        assertObjectsEqual(result1, result2);

        verify(hotelRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should evict 'hotels' cache when saving a new Hotel")
    void shouldUpdateCacheWhenSavingNewHotel() {
        var hotel = Hotel.builder().build();
        var existingHotels = createPage(0, 10, List.of(hotel));

        when(hotelRepository.save(hotel)).thenReturn(hotel);

        assertCacheKeyCount(0);

        populateCache("hotels::Pagination(pageNumber=0, pageSize=10)", existingHotels);

        assertCacheKeyCount(1);

        hotelService.save(hotel);

        assertCacheDoesNotContainKey("hotels::*");
    }

    @Test
    @DisplayName("Should update 'hotel' cache and evict 'hotels' cache when updating an existing Hotel")
    void shouldUpdateCacheWhenUpdatingExistingHotel() {
        var hotel = Hotel.builder().id(1L).name("Updated Hotel").build();
        var existingHotel = Hotel.builder().id(1L).name("Existing Hotel").build();
        var existingHotels = createPage(0, 10, List.of(existingHotel));

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(existingHotel));
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        assertCacheKeyCount(0);

        populateCache("hotels::Pagination(pageNumber=0, pageSize=10)", existingHotels);
        populateCache("hotel::1", existingHotel);

        assertCacheKeyCount(2);

        hotelService.update(hotel);

        var cachedHotel = getCacheValue("hotel::1");
        assertObjectsEqual(cachedHotel, hotel);
        assertCacheDoesNotContainKey("hotels::*");
    }

    @Test
    @DisplayName("Should evict cache when deleting a Hotel")
    void shouldEvictCacheWhenDeletingHotel() {
        var hotelId = 1L;
        var existingHotel = Hotel.builder().id(1L).name("Existing Hotel").build();
        var existingHotels = createPage(0, 10, List.of(existingHotel));

        doNothing().when(hotelRepository).deleteById(hotelId);

        assertCacheKeyCount(0);

        populateCache("hotels::Pagination(pageNumber=0, pageSize=10)", existingHotels);
        populateCache("hotel::1", existingHotel);

        assertCacheKeyCount(2);

        hotelService.deleteById(hotelId);

        assertCacheDoesNotContainKey("hotel::1");
        assertCacheDoesNotContainKey("hotels::*");
    }
}
