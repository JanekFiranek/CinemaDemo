package tech.firanek.cinemademo.data;

import lombok.Value;

import java.time.Instant;

@Value
public class ReservationResponse {
    double amountToPay;
    Instant expirationDate;
}
