package example.bookify.server.service.impl;

import example.bookify.server.service.HotelRatingService;
import example.bookify.server.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelRatingServiceImpl implements HotelRatingService {

    private final HotelService hotelService;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "hotel", key = "#id"),
            @CacheEvict(value = "hotels", allEntries = true)
    })
    public void rate(Long id, Integer score) {
        var hotel = hotelService.findById(id);

        var currentRating = hotel.getRating();
        var ratingsCount = hotel.getRatingsCount();
        var newRating = Math.round((currentRating * ratingsCount + score) / (ratingsCount + 1) * 10) / 10.0F;

        hotel.setRating(newRating);
        hotel.setRatingsCount(ratingsCount + 1);

        hotelService.update(hotel);
    }
}
