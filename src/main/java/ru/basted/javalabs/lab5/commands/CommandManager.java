package ru.basted.javalabs.lab5.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.basted.javalabs.lab5.util.ParseTools;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final List<String> commandsInformation = new ArrayList<>();

    public CommandManager(Connection connection) {
        commandsInformation.add("Доступные команды:\n\n");

        register(new AddCommand(connection));
        register(new ShowCommand(connection));
        register(new UpdateCommand(connection));
        register(new RemoveCommand(connection));
        register(new HelpCommand(commandsInformation));
        register(new ExitCommand());

        commandsInformation.addAll(ParseTools.getFormatsInformation());
    }

    public List<String> execute(String rawInput) throws SQLException {
        String[] args = rawInput.trim().split("\\s+", 2);

        Command command = commands.get(args[0].toLowerCase());
        if (command == null) {
            return List.of(
                    "Ошибка: Неизвестная команда «" + args[0] + "». " +
                            "Введите help, чтобы увидеть список доступных команд.\n"
            );
        }

        String rawArgs = (args.length > 1) ? args[1] : "";

        return command.execute(rawArgs);
    }

    private void register(Command command) {
        commands.put(command.getName().toLowerCase(), command);

        List<String> usage = command.getUsage();
        String usageValue = (usage.isEmpty()) ? "" : " " + String.join("\n  " + command.getName() + " ", usage);

        commandsInformation.add("  " + command.getName() + usageValue + "\n\t" + command.getDescription() + "\n\n");
    }
}
