package ru.basted.javalabs.lab7.model;

import java.time.LocalDateTime;

public class WinningTicket {
    private int drawNumber;
    private int ticketNumber;
    private int prizeAmount;
    private LocalDateTime dateTime;

    public WinningTicket(int ticketNumber, int prizeAmount) {
        this(0, ticketNumber, prizeAmount, null);
    }

    public WinningTicket(int drawNumber, int ticketNumber, int prizeAmount, LocalDateTime dateTime) {
        this.drawNumber = drawNumber;
        this.ticketNumber = ticketNumber;
        this.prizeAmount = prizeAmount;
        this.dateTime = dateTime;
    }

    public int getDrawNumber() {
        return drawNumber;
    }

    public void setDrawNumber(int drawNumber) {
        this.drawNumber = drawNumber;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public int getPrizeAmount() {
        return prizeAmount;
    }

    public void setPrizeAmount(int prizeAmount) {
        this.prizeAmount = prizeAmount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
