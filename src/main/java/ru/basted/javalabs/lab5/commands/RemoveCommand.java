package ru.basted.javalabs.lab5.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import ru.basted.javalabs.lab5.common.DBSchema;
import ru.basted.javalabs.lab5.model.WinningTicket;
import ru.basted.javalabs.lab5.util.ParseTools;

public class RemoveCommand extends BaseCommand {
    private final Connection connection;

    public RemoveCommand(Connection connection) {
        super("remove", "Удаление записи о тираже из базы данных", List.of("<тираж>"));

        this.connection = connection;
    }

    @Override
    public List<String> execute(String rawArgs) throws SQLException {
        String[] args = rawArgs.trim().split(";");
        if (args.length != 1) {
            throw new IllegalArgumentException(
                    "Неверное количество параметров.\nИспользование: %s %s"
                            .formatted(getName(), getUsage().getFirst())
            );
        }

        WinningTicket winningTicket = new WinningTicket(ParseTools.parseInteger(args[0]));

        String removeQuery = "DELETE FROM %s WHERE %s = ?".formatted(DBSchema.TABLE_NAME, DBSchema.DRAW_NUMBER);

        int rowsAffected = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(removeQuery)) {
            preparedStatement.setInt(1, winningTicket.drawNumber());

            rowsAffected = preparedStatement.executeUpdate();
        }

        if (rowsAffected == 0) {
            throw new IllegalArgumentException(
                    "Информация о тираже №" + winningTicket.drawNumber()
                            + " отсутствует в базе данных. Удаление не представляется возможным."
            );
        }

        return List.of("Из таблицы успешно удалена информация о тираже №" + winningTicket.drawNumber() + "\n");
    }
}
