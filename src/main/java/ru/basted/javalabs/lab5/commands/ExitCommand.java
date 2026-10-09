package ru.basted.javalabs.lab5.commands;

import java.sql.SQLException;
import java.util.List;

public class ExitCommand extends BaseCommand {
    public ExitCommand() {
        super("exit", "Завершить работу клиента.", List.of());
    }

    @Override
    public List<String> execute(String rawArgs) throws SQLException {
        throw new IllegalStateException("Команда exit обрабатывается в TicketRepository и не должна вызывать execute()");
    }
}
