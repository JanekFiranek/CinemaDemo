package tech.firanek.cinemademo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.firanek.cinemademo.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
