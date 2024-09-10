package example.bookify.server.service;

import example.bookify.server.model.Hotel;
import example.bookify.server.web.dto.request.Pagination;
import example.bookify.server.web.dto.request.filter.HotelFilter;
import org.springframework.data.domain.Page;

public interface HotelService {

    Page<Hotel> findAll(Pagination pagination);

    Page<Hotel> findByFilter(HotelFilter filter);

    Hotel findById(Long id);

    Hotel save(Hotel hotel);

    Hotel update(Hotel hotel);

    void deleteById(Long id);
}
