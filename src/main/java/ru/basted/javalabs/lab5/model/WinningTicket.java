package ru.basted.javalabs.lab5.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record WinningTicket(
        int drawNumber,
        int ticketNumber,
        int prizeAmount,
        LocalDateTime drawDate
) {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yy");

    public WinningTicket(int drawNumber) {
        this(drawNumber, 0, 0, null);
    }

    public WinningTicket(int ticketNumber, int prizeAmount, LocalDateTime drawDate) {
        this(0, ticketNumber, prizeAmount, drawDate);
    }

    @Override
    public String toString() {
        return "Тираж №%d (от %s года)\nВыиграл билет №%d\nСумма выигрыша: %d руб."
                .formatted(drawNumber, drawDate.format(formatter), ticketNumber, prizeAmount);
    }
}
