package tech.firanek.cinemademo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TicketType type;
    private int row;
    private int seat;

    public Ticket(TicketType type, int row, int seat) {
        this.type = type;
        this.row = row;
        this.seat = seat;
    }
}
