package example.bookify.server.service.impl;

import example.bookify.server.model.Hotel;
import example.bookify.server.service.HotelRatingService;
import example.bookify.server.service.HotelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.mockito.Mockito.when;

@DisplayName("HotelRatingService Cache Tests")
@ContextConfiguration(classes = {
        HotelRatingService.class,
        HotelRatingServiceImpl.class
})
class HotelRatingServiceImplCacheTests extends AbstractServiceCacheTest {

    @Autowired
    private HotelRatingService hotelRatingService;

    @MockBean
    private HotelService hotelService;

    @Test
    @DisplayName("Should evict 'hotel' and 'hotels' caches when a hotel is rated")
    void shouldEvictCachesWhenHotelIsRated() {
        var hotelId = 1L;
        var score = 5;
        var hotel = Hotel.builder()
                .id(hotelId)
                .rating(4.0F)
                .ratingsCount(10)
                .build();
        var existingHotels = createPage(0, 10, List.of(hotel));

        when(hotelService.findById(hotelId)).thenReturn(hotel);

        assertCacheKeyCount(0);

        populateCache("hotel::" + hotelId, hotel);
        populateCache("hotels::Pagination(pageNumber=0, pageSize=10)", existingHotels);

        assertCacheKeyCount(2);

        hotelRatingService.rate(hotelId, score);

        assertCacheDoesNotContainKey("hotel::" + hotelId);
        assertCacheDoesNotContainKey("hotels::*");
    }
}
