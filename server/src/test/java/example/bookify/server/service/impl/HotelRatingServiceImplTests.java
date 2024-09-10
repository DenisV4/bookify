package example.bookify.server.service.impl;

import example.bookify.server.model.Hotel;
import example.bookify.server.service.HotelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("HotelRatingServiceImpl Unit Tests")
class HotelRatingServiceImplTests {

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private HotelRatingServiceImpl hotelRatingService;

    @Test
    @DisplayName("Should correctly update rating and count when rating 5 is submitted to a hotel with initial rating 4.2 and 5 ratings")
    void shouldUpdateRatingAndCountForExistingRatings() {
        var hotelId = 1L;
        var newScore = 5;

        var existingHotel = Hotel.builder()
                .id(hotelId)
                .rating(4.2F)
                .ratingsCount(5)
                .build();

        when(hotelService.findById(hotelId)).thenReturn(existingHotel);

        hotelRatingService.rate(hotelId, newScore);

        var hotelCaptor = ArgumentCaptor.forClass(Hotel.class);
        verify(hotelService).update(hotelCaptor.capture());

        var updatedHotel = hotelCaptor.getValue();
        System.out.println(updatedHotel.getRating());
        assertThat(updatedHotel.getRating()).isEqualTo(4.3F);
        assertThat(updatedHotel.getRatingsCount()).isEqualTo(6);
    }

    @Test
    @DisplayName("Should correctly update rating and count when rating 5 is submitted to a hotel with no initial ratings")
    void shouldUpdateRatingAndCountForNoInitialRatings() {
        var hotelId = 1L;
        var newScore = 5;

        var existingHotel = Hotel.builder()
                .id(hotelId)
                .rating(0.0F)
                .ratingsCount(0)
                .build();

        when(hotelService.findById(hotelId)).thenReturn(existingHotel);

        hotelRatingService.rate(hotelId, newScore);

        var hotelCaptor = ArgumentCaptor.forClass(Hotel.class);
        verify(hotelService).update(hotelCaptor.capture());

        var updatedHotel = hotelCaptor.getValue();

        assertThat(updatedHotel.getRating()).isEqualTo(5.0F);
        assertThat(updatedHotel.getRatingsCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should correctly update rating and count when rating 3 is submitted to a hotel with initial rating 4 and 1 rating")
    void shouldUpdateRatingAndCountForSingleExistingRating() {
        var hotelId = 1L;
        var newScore = 3;

        var existingHotel = Hotel.builder()
                .id(hotelId)
                .rating(4.0F)
                .ratingsCount(1)
                .build();

        when(hotelService.findById(hotelId)).thenReturn(existingHotel);

        hotelRatingService.rate(hotelId, newScore);

        var hotelCaptor = ArgumentCaptor.forClass(Hotel.class);
        verify(hotelService).update(hotelCaptor.capture());

        var updatedHotel = hotelCaptor.getValue();

        assertThat(updatedHotel.getRating()).isEqualTo(3.5F);
        assertThat(updatedHotel.getRatingsCount()).isEqualTo(2);
    }
}
