package tech.firanek.cinemademo.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import tech.firanek.cinemademo.data.ReservationRequest;
import tech.firanek.cinemademo.data.ReservationResponse;
import tech.firanek.cinemademo.data.ScreeningResponse;
import tech.firanek.cinemademo.entity.Reservation;
import tech.firanek.cinemademo.entity.Room;
import tech.firanek.cinemademo.entity.Screening;
import tech.firanek.cinemademo.entity.Ticket;
import tech.firanek.cinemademo.repository.ReservationRepository;
import tech.firanek.cinemademo.repository.ScreeningRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CinemaService {

    private final ReservationRepository reservations;
    private final ScreeningRepository screenings;

    public CinemaService(ReservationRepository reservations, ScreeningRepository screenings) {
        this.reservations = reservations;
        this.screenings = screenings;
    }

    public ScreeningResponse handleScreeningResponse(final String id) {
        final Screening screening = this.screenings.getReferenceById(Long.parseLong(id));
        final Room room = screening.getRoom();

        final Multimap<Integer, Integer> seats = ArrayListMultimap.create();
        for (int row = 1; row <= room.getNumberOfRows(); row++) {
            for (int seat = 1; seat <= room.getSeatsPerRow(); seat++) {
                seats.put(row, seat);
            }
        }

        for (Ticket ticket : this.getAllScreeningTickets(screening)) {
            seats.remove(ticket.getRow(), ticket.getSeat());
        }

        return new ScreeningResponse(room, seats.asMap());
    }

    public List<Screening> handleScreeningRequest(final Instant from, final Instant to) {
        return this.screenings.findAllByTimeBetweenOrderByMovieTitleAscTimeAsc(from, to);
    }

    public ReservationResponse handleReservationResponse(final ReservationRequest reservationRequest) {
        if (this.screenings.existsById(reservationRequest.getScreeningId())) {
            final Screening screening = this.screenings.getReferenceById(reservationRequest.getScreeningId());
            final Room room = screening.getRoom();
            final Set<Ticket> alreadyBoughtTickets = this.getAllScreeningTickets(screening);

            if (screening.getTime().minus(Duration.ofMinutes(15)).isBefore(Instant.now())) {
                throw new ValidationException("Cannot reserve sooner than 15 minutes before screening");
            }

            if (reservationRequest.getTickets().stream()
                    .collect(Collectors.groupingBy(Ticket::getSeat))
                    .values().stream().anyMatch(n -> n.size() > 1)) {
                throw new ValidationException("Provided multiple tickets for one seat");
            }

            for (Ticket ticket : reservationRequest.getTickets()) {
                if (ticket.getRow() > room.getNumberOfRows() || ticket.getRow() <= 0) {
                    throw new ValidationException("Provided an nonexistent row number");
                }

                if (ticket.getSeat() > room.getSeatsPerRow() || ticket.getSeat() <= 0) {
                    throw new ValidationException("Provided an nonexistent seat number");
                }

                if (alreadyBoughtTickets.stream()
                        .anyMatch(other -> other.getSeat() == ticket.getSeat())) {
                    throw new ValidationException("Provided a seat that is already booked");
                }

                Map<Integer, List<Ticket>> ticketsByRows = reservationRequest.getTickets()
                        .stream().collect(Collectors.groupingBy(Ticket::getRow));

                for (List<Ticket> row : ticketsByRows.values()) {
                    row.sort(Comparator.comparingInt(Ticket::getSeat));
                    if (!this.areSeatsNeighboring(row)) {
                        throw new ValidationException("There is an empty space between selected seats");
                    }
                }
            }

            final Reservation reservation = new Reservation(reservationRequest.getName(), reservationRequest.getSurname(),
                    screening, reservationRequest.getTickets());
            this.reservations.save(reservation);
            return new ReservationResponse(this.calculateAmountToPay(reservationRequest),
                    screening.getTime().plus(screening.getMovie().getDuration()));
        } else {
            throw new ValidationException("No entity associated with provided id " + reservationRequest.getScreeningId());
        }
    }

    private double calculateAmountToPay(final ReservationRequest request) {
        return request.getTickets().stream()
                .mapToDouble(n -> n.getType().getPrice()).sum();
    }

    private Set<Ticket> getAllScreeningTickets(final Screening screening) {
        return this.reservations.getReservationsByScreening(screening)
                .stream().map(Reservation::getTickets)
                .flatMap(Set::stream).collect(Collectors.toSet());
    }

    private boolean areSeatsNeighboring(final List<Ticket> row) {
        for (int i = 0; i < row.size() - 1; i++) {
            if (row.get(i).getSeat() != row.get(i + 1).getSeat() - 1) {
                return false;
            }
        }

        return true;
    }
}
