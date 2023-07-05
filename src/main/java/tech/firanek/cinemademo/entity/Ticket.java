package tech.firanek.cinemademo.entity;

import jakarta.persistence.*;
import tech.firanek.cinemademo.misc.TicketType;

@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Reservation reservation;
    @Enumerated(EnumType.STRING)
    private TicketType type;
    private int row;
    private int seat;
}
