package tech.firanek.cinemademo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.firanek.cinemademo.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
