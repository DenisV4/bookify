package example.bookify.server.service;

import example.bookify.server.model.Room;
import example.bookify.server.web.dto.request.Pagination;
import example.bookify.server.web.dto.request.filter.RoomFilter;
import org.springframework.data.domain.Page;

public interface RoomService {

    Page<Room> findAll(Pagination pagination);

    Room findById(Long id);

    Page<Room> findByFilter(RoomFilter filter);

    Room save(Room room);

    Room update(Room room);

    void deleteById(Long id);
}
