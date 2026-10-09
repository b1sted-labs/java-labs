package ru.basted.javalabs.lab5.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.basted.javalabs.lab5.common.DBSchema;
import ru.basted.javalabs.lab5.model.WinningTicket;
import ru.basted.javalabs.lab5.util.ParseTools;

public class ShowCommand extends BaseCommand {
    private final Connection connection;

    public ShowCommand(Connection connection) {
        super("show", "Показать информацию всех тиражей или только указанного.", List.of("[тираж]"));

        this.connection = connection;
    }

    @Override
    public List<String> execute(String rawArgs) throws SQLException {
        String[] args = rawArgs.trim().split(";");
        if (args.length > 1) {
            throw new IllegalArgumentException(
                    "Неверное количество параметров.\nИспользование: %s %s"
                            .formatted(getName(), getUsage().getFirst())
            );
        }

        return (args.length == 0 || args[0].isEmpty())
                ? selectAllStrings()
                : selectSpecificString(ParseTools.parseInteger(args[0]));
    }

    private List<String> selectAllStrings() throws SQLException {
        List<String> strings = new ArrayList<>();

        String selectQuery = "SELECT * FROM %s".formatted(DBSchema.TABLE_NAME);

        try (Statement statement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = statement.executeQuery(selectQuery)) {
            while (resultSet.next()) {
                WinningTicket winningTicket = new WinningTicket(
                        resultSet.getInt(DBSchema.DRAW_NUMBER),
                        resultSet.getInt(DBSchema.TICKET_NUMBER),
                        resultSet.getInt(DBSchema.PRIZE_AMOUNT),
                        resultSet.getObject(DBSchema.DRAW_DATE, LocalDateTime.class)
                );

                strings.add(winningTicket + "\n");
                strings.add("\n");
            }
        }

        if (!strings.isEmpty()) {
            strings.removeLast();
        }

        return (strings.isEmpty()) ? List.of("Нет данных о тиражах\n") : strings;
    }

    private List<String> selectSpecificString(int drawNumber) throws SQLException {
        String selectQuery = "SELECT * FROM %s WHERE %s = ?".formatted(DBSchema.TABLE_NAME, DBSchema.DRAW_NUMBER);

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, drawNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    WinningTicket winningTicket = new WinningTicket(
                            resultSet.getInt(DBSchema.DRAW_NUMBER),
                            resultSet.getInt(DBSchema.TICKET_NUMBER),
                            resultSet.getInt(DBSchema.PRIZE_AMOUNT),
                            resultSet.getObject(DBSchema.DRAW_DATE, LocalDateTime.class)
                    );

                    return List.of(winningTicket + "\n");
                }
            }
        }

        throw new IllegalArgumentException("Информация о тираже №" + drawNumber + " отсутствует в базе данных!");
    }
}
