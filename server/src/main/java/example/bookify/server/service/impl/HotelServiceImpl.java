package example.bookify.server.service.impl;

import lombok.RequiredArgsConstructor;
import example.bookify.server.exception.ResourceNotFoundException;
import example.bookify.server.model.Hotel;
import example.bookify.server.repository.HotelRepository;
import example.bookify.server.repository.specification.HotelSpecification;
import example.bookify.server.service.HotelService;
import example.bookify.server.util.BeanUtil;
import example.bookify.server.web.dto.request.Pagination;
import example.bookify.server.web.dto.request.filter.HotelFilter;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    @Cacheable(value = "hotels", keyGenerator = "keyGenerator")
    public Page<Hotel> findAll(Pagination pagination) {
        var pageRequest = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(), Sort.by("id"));
        return hotelRepository.findAll(pageRequest);
    }

    @Override
    @Cacheable(value = "hotels", keyGenerator = "keyGenerator")
    public Page<Hotel> findByFilter(HotelFilter filter) {
        var specification = HotelSpecification.withFilter(filter);
        var pageRequest = PageRequest.of(filter.getPageNumber(), filter.getPageSize(), Sort.by("id"));

        return hotelRepository.findAll(specification, pageRequest);
    }

    @Override
    @Cacheable(value = "hotel", keyGenerator = "keyGenerator")
    public Hotel findById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(ResourceNotFoundException.supply("Hotel with id={0} was not found", id));
    }

    @Override
    @CacheEvict(value = "hotels", allEntries = true)
    public Hotel save(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    @CachePut(value = "hotel", key = "#hotel.id")
    @CacheEvict(value = "hotels", allEntries = true)
    public Hotel update(Hotel hotel) {
        var existingHotel = findById(hotel.getId());
        BeanUtil.copyNonNullProperties(hotel, existingHotel);

        return hotelRepository.save(existingHotel);
    }

    @Caching(evict = {
            @CacheEvict(value = "hotel", key = "#id"),
            @CacheEvict(value = "hotels", allEntries = true)
    })
    @Override
    public void deleteById(Long id) {
        hotelRepository.deleteById(id);
    }
}
