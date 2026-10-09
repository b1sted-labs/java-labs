package ru.basted.javalabs.lab5.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import ru.basted.javalabs.lab5.common.DBSchema;
import ru.basted.javalabs.lab5.model.WinningTicket;
import ru.basted.javalabs.lab5.util.ParseTools;

public class AddCommand extends BaseCommand {
    private final Connection connection;

    public AddCommand(Connection connection) {
        super(
                "add",
                "Добавление информации о проведенной лотерее.",
                List.of("<билет>;<выигрыш>", "<билет>;<выигрыш>;<дата_розыгрыша>")
        );

        this.connection = connection;
    }

    @Override
    public List<String> execute(String rawArgs) throws SQLException {
        String[] args = rawArgs.trim().split(";");
        if (args.length != 2 && args.length != 3) {
            String name = getName();
            List<String> usage = getUsage();

            throw new IllegalArgumentException(
                    "Неверное количество параметров.\nИспользование: %s %s\n%15s%s %s"
                            .formatted(name, usage.getFirst(), " ", name, usage.getLast())
            );
        }

        boolean hasDate = args.length == 3;

        WinningTicket winningTicket = new WinningTicket(
                ParseTools.parseInteger(args[0]),
                ParseTools.parseInteger(args[1]),
                hasDate ? ParseTools.parseDateTime(args[2]) : null
        );

        String addQuery = hasDate
                ? "INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)"
                .formatted(DBSchema.TABLE_NAME, DBSchema.TICKET_NUMBER, DBSchema.PRIZE_AMOUNT, DBSchema.DRAW_DATE)
                : "INSERT INTO %s (%s, %s) VALUES (?, ?)"
                .formatted(DBSchema.TABLE_NAME, DBSchema.TICKET_NUMBER, DBSchema.PRIZE_AMOUNT);

        try (PreparedStatement preparedStatement = connection.prepareStatement(addQuery)) {
            preparedStatement.setInt(1, winningTicket.ticketNumber());
            preparedStatement.setInt(2, winningTicket.prizeAmount());

            if (winningTicket.drawDate() != null) {
                preparedStatement.setObject(3, winningTicket.drawDate());
            }

            preparedStatement.executeUpdate();
        }

        return List.of("В таблицу успешно добавлена информация о проведении тиража: " + rawArgs + "\n");
    }
}
