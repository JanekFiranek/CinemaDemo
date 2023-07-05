package tech.firanek.cinemademo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.firanek.cinemademo.entity.Screening;

import java.time.Instant;
import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findAllByTimeBetweenOrderByMovieTitleAscTimeAsc(Instant from, Instant to);
}
