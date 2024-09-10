package example.bookify.server.service.impl;

import example.bookify.server.exception.ResourceNotFoundException;
import example.bookify.server.model.Room;
import example.bookify.server.repository.RoomRepository;
import example.bookify.server.repository.specification.RoomSpecification;
import example.bookify.server.service.RoomService;
import example.bookify.server.util.BeanUtil;
import example.bookify.server.web.dto.request.Pagination;
import example.bookify.server.web.dto.request.filter.RoomFilter;
import lombok.RequiredArgsConstructor;
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
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    @Cacheable(value = "rooms", keyGenerator = "keyGenerator")
    public Page<Room> findAll(Pagination pagination) {
        var pageRequest = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(), Sort.by("id"));
        return roomRepository.findAll(pageRequest);
    }

    @Override
    @Cacheable(value = "rooms", keyGenerator = "keyGenerator")
    public Page<Room> findByFilter(RoomFilter filter) {
        var specification = RoomSpecification.withFilter(filter);
        var pageRequest = PageRequest.of(filter.getPageNumber(), filter.getPageSize(), Sort.by("id"));

        return roomRepository.findAll(specification, pageRequest);
    }

    @Override
    @Cacheable(value = "room", keyGenerator = "keyGenerator")
    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(ResourceNotFoundException.supply("Room with id={0} was not found", id));
    }

    @Override
    @CacheEvict(value = "rooms", allEntries = true)
    public Room save(Room room) {
        return roomRepository.save(room);
    }

    @Override
    @CachePut(value = "room", key = "#room.id")
    @CacheEvict(value = "rooms", allEntries = true)
    public Room update(Room room) {
        var existingRoom = findById(room.getId());
        BeanUtil.copyNonNullProperties(room, existingRoom);

        return roomRepository.save(existingRoom);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "room", key = "#id"),
            @CacheEvict(value = "rooms", allEntries = true)
    })
    public void deleteById(Long id) {
        roomRepository.deleteById(id);
    }
}
