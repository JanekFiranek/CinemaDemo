package tech.firanek.cinemademo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Ticket> tickets;
    @OneToOne
    private Screening screening;

    public Reservation(String name, String surname, Screening screening, Set<Ticket> tickets) {
        this.name = name;
        this.surname = surname;
        this.screening = screening;
        this.tickets = tickets;
    }
}
