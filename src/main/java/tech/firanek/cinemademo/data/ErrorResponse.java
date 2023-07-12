package tech.firanek.cinemademo.data;

import lombok.Value;

import java.time.Instant;

@Value
public class ErrorResponse {
    Instant timestamp;
    String message;
}
