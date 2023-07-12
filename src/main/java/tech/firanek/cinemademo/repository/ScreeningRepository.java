package tech.firanek.cinemademo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.firanek.cinemademo.entity.Screening;

import java.time.Instant;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findAllByTimeBetweenOrderByMovieTitleAscTimeAsc(Instant from, Instant to);
}
