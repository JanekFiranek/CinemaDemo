package tech.firanek.cinemademo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.firanek.cinemademo.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
