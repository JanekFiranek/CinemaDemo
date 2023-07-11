package tech.firanek.cinemademo.data;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;
import tech.firanek.cinemademo.entity.Ticket;

import java.util.Set;

@Value
public class ReservationRequest {
    @Size(min = 3, message = "Provided name is too short")
    @Pattern(regexp = "\\p{Lu}\\p{Ll}*", message = "Provided name is invalid")
    String name;
    @Size(min = 3, message = "Provided surname is too short")
    @Pattern(regexp = "^\\p{Lu}\\p{Ll}*(?:-\\p{Lu}\\p{Ll}*)?$", message = "Provided surname is invalid")
    String surname;
    Long screeningId;
    @Size(min = 1, message = "Did not provide any tickets")
    Set<Ticket> tickets;
}
