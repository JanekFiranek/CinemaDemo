package tech.firanek.cinemademo.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import tech.firanek.cinemademo.data.ReservationRequest;
import tech.firanek.cinemademo.data.ReservationResponse;
import tech.firanek.cinemademo.data.ScreeningResponse;
import tech.firanek.cinemademo.entity.Screening;
import tech.firanek.cinemademo.service.CinemaService;

import java.time.Instant;
import java.util.List;

@RestController
public class CinemaController {

    private final CinemaService service;

    CinemaController(CinemaService service) {
        this.service = service;
    }

    @GetMapping("/screenings")
    public List<Screening> getScreenings(@RequestParam("from") Instant from, @RequestParam("to") Instant to) {
        return this.service.handleScreeningRequest(from, to);
    }

    @GetMapping("/screenings/{id}")
    public ScreeningResponse getScreening(@PathVariable String id) {
        return this.service.handleScreeningResponse(id);
    }

    @PostMapping("/reservations")
    public ReservationResponse makeReservation(@Valid @RequestBody ReservationRequest reservationRequest) {
        return this.service.handleReservationResponse(reservationRequest);
    }
}
