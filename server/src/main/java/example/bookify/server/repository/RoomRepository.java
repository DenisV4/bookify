package example.bookify.server.repository;

import example.bookify.server.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
}
