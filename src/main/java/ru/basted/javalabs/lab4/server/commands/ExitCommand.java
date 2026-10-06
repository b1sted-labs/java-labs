package ru.basted.javalabs.lab4.server.commands;

import java.util.List;

public class ExitCommand extends BaseCommand {
    public ExitCommand() {
        super("exit", "Завершить работу клиента.", List.of());
    }

    @Override
    public List<String> execute(String rawArgs) {
        throw new IllegalStateException("Команда exit обрабатывается в ClientHandler и не должна вызывать execute()");
    }
}
