package ru.basted.javalabs.lab7.dao;

import java.sql.SQLException;
import java.util.List;

import ru.basted.javalabs.lab7.model.WinningTicket;

public interface WinningTicketDao {
    List<WinningTicket> findAll() throws SQLException;
    WinningTicket findByDrawNumber(int drawNumber) throws SQLException;

    void insertWinningTicket(WinningTicket winningTicket) throws SQLException;
    void updateWinningTicket(int drawNumber, WinningTicket winningTicket) throws SQLException;
    void removeWinningTicket(int drawNumber) throws SQLException;
}
