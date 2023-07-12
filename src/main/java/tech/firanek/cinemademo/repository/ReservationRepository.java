package tech.firanek.cinemademo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.firanek.cinemademo.entity.Reservation;
import tech.firanek.cinemademo.entity.Screening;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> getReservationsByScreening(Screening screening);
}
