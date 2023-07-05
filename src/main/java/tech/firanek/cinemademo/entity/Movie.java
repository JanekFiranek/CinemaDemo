package tech.firanek.cinemademo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Movie {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private Duration duration;
    public Movie(String title, Duration duration) {
        this.title = title;
        this.duration = duration;
    }
}
