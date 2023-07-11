package tech.firanek.cinemademo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant time;
    @ManyToOne
    private Movie movie;
    @ManyToOne
    private Room room;
    public Screening(Instant time, Movie movie, Room room) {
        this.time = time;
        this.movie = movie;
        this.room = room;
    }


}
