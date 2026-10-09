package ru.basted.javalabs.lab5.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import ru.basted.javalabs.lab5.common.DBSchema;
import ru.basted.javalabs.lab5.model.WinningTicket;
import ru.basted.javalabs.lab5.util.ParseTools;

public class UpdateCommand extends BaseCommand {
    private final Connection connection;

    public UpdateCommand(Connection connection) {
        super(
                "update",
                "Обновление информации о тираже",
                List.of("<тираж>;<билет>;<выигрыш>;<дата_розыгрыша>")
        );

        this.connection = connection;
    }

    @Override
    public List<String> execute(String rawArgs) throws SQLException {
        String[] args = rawArgs.trim().split(";");
        if (args.length != 4) {
            throw new IllegalArgumentException(
                    "Неверное количество параметров.\nИспользование: %s %s"
                            .formatted(getName(), getUsage().getFirst())
            );
        }

        WinningTicket winningTicket = new WinningTicket(
                ParseTools.parseInteger(args[0]),
                ParseTools.parseInteger(args[1]),
                ParseTools.parseInteger(args[2]),
                ParseTools.parseDateTime(args[3])
        );

        String updateQuery = """
                UPDATE %s
                SET %s = ?, %s = ?, %s = ?
                WHERE %s = ?;
                """.formatted(DBSchema.TABLE_NAME, DBSchema.TICKET_NUMBER, DBSchema.PRIZE_AMOUNT, DBSchema.DRAW_DATE,
                DBSchema.DRAW_NUMBER);

        int rowsAffected = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setInt(1, winningTicket.ticketNumber());
            preparedStatement.setInt(2, winningTicket.prizeAmount());
            preparedStatement.setObject(3, winningTicket.drawDate());
            preparedStatement.setInt(4, winningTicket.drawNumber());

            rowsAffected = preparedStatement.executeUpdate();
        }

        if (rowsAffected == 0) {
            throw new IllegalArgumentException(
                    "Информация о тираже №" + winningTicket.drawNumber()
                            + " отсутствует в базе данных. Обновление не представляется возможным."
            );
        }

        return List.of("Успешно обновлена информация о тираже: " + rawArgs + "\n");
    }
}
