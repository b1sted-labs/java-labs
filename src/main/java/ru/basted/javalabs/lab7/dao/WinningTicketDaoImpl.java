package ru.basted.javalabs.lab7.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ru.basted.javalabs.lab7.db.DBConnector;
import ru.basted.javalabs.lab7.db.DBSchema;
import ru.basted.javalabs.lab7.model.WinningTicket;

public class WinningTicketDaoImpl implements WinningTicketDao {
    @Override
    public List<WinningTicket> findAll() throws SQLException {
        List<WinningTicket> winningTickets = new ArrayList<>();
        String query = "SELECT * FROM %s".formatted(DBSchema.TABLE_NAME);

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery(query)) {
            while (resultSet.next()) {
                WinningTicket winningTicket = new WinningTicket(
                        resultSet.getInt(DBSchema.DRAW_NUMBER),
                        resultSet.getInt(DBSchema.TICKET_NUMBER),
                        resultSet.getInt(DBSchema.PRIZE_AMOUNT),
                        resultSet.getObject(DBSchema.DRAW_DATE, LocalDateTime.class)
                );

                winningTickets.add(winningTicket);
            }
        }

        return winningTickets;
    }

    @Override
    public WinningTicket findByDrawNumber(int drawNumber) throws SQLException {
        if (drawNumber < 1) {
            throw new IllegalArgumentException("Номер тиражей начинается с 1");
        }

        String query = "SELECT * FROM %s WHERE %s = %d".formatted(DBSchema.TABLE_NAME, DBSchema.DRAW_NUMBER, drawNumber);

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery(query)) {
            if (resultSet.next()) {
                return new WinningTicket(
                        resultSet.getInt(DBSchema.DRAW_NUMBER),
                        resultSet.getInt(DBSchema.TICKET_NUMBER),
                        resultSet.getInt(DBSchema.PRIZE_AMOUNT),
                        resultSet.getObject(DBSchema.DRAW_DATE, LocalDateTime.class)
                );
            }
        }

        throw new IllegalArgumentException("Выигрышного билета с ID " + drawNumber + " нет в базе данных!");
    }

    @Override
    public void insertWinningTicket(WinningTicket winningTicket) throws SQLException {
        String query = "INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)"
                .formatted(DBSchema.TABLE_NAME, DBSchema.TICKET_NUMBER, DBSchema.PRIZE_AMOUNT, DBSchema.DRAW_DATE);

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, winningTicket.getTicketNumber());
            preparedStatement.setInt(2, winningTicket.getPrizeAmount());
            preparedStatement.setObject(3, winningTicket.getDateTime());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateWinningTicket(int drawNumber, WinningTicket winningTicket) throws SQLException {
        if (drawNumber < 1) {
            throw new IllegalArgumentException("Номер тиражей начинается с 1");
        }

        String query = "UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?"
                .formatted(DBSchema.TABLE_NAME, DBSchema.TICKET_NUMBER, DBSchema.PRIZE_AMOUNT, DBSchema.DRAW_DATE,
                        DBSchema.DRAW_NUMBER);

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, winningTicket.getTicketNumber());
            preparedStatement.setInt(2, winningTicket.getPrizeAmount());
            preparedStatement.setObject(3, winningTicket.getDateTime());

            preparedStatement.setInt(4, drawNumber);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException(
                        "Информация о тираже №" + winningTicket.getDrawNumber()
                                + " отсутствует в базе данных. Обновление не представляется возможным."
                );
            }
        }
    }

    @Override
    public void removeWinningTicket(int drawNumber) throws SQLException {
        if (drawNumber < 1) {
            throw new IllegalArgumentException("Номер тиражей начинается с 1");
        }

        String query = "DELETE FROM %s WHERE %s = ?".formatted(DBSchema.TABLE_NAME, DBSchema.DRAW_NUMBER);

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, drawNumber);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException(
                        "Информация о тираже №" + drawNumber
                                + " отсутствует в базе данных. Обновление не представляется возможным."
                );
            }
        }
    }
}
