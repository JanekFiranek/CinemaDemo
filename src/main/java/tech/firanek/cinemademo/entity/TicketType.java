package tech.firanek.cinemademo.entity;

import lombok.Getter;

public enum TicketType {

    ADULT(25),
    STUDENT(18),
    CHILD(12.50);

    @Getter
    private final double price;

    TicketType(double price) {
        this.price = price;
    }
}
