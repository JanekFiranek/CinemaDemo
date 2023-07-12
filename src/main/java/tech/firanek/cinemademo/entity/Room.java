package tech.firanek.cinemademo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Room {
    @Id
    private Long id;
    private int numberOfRows;
    private int seatsPerRow;
}
