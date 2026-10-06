package ru.basted.javalabs.lab4.server.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.basted.javalabs.lab4.server.schedules.ScheduleRepository;
import ru.basted.javalabs.lab4.server.util.ParseTools;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final List<String> commandsInformation = new ArrayList<>();

    public CommandManager(ScheduleRepository scheduleRepository) {
        commandsInformation.add("Доступные команды:\n");

        register(new AddCommand(scheduleRepository));
        register(new ShowCommand(scheduleRepository));
        register(new UpdateCommand(scheduleRepository));
        register(new RemoveCommand(scheduleRepository));
        register(new RenameCommand(scheduleRepository));
        register(new HelpCommand(commandsInformation));
        register(new ExitCommand());

        commandsInformation.add("\n");
        commandsInformation.addAll(ParseTools.getFormatsInformation());
    }

    public List<String> execute(String rawInput) {
        String[] args = rawInput.trim().split("\\s+", 2);

        Command command = commands.get(args[0].toLowerCase());
        if (command == null) {
            return List.of(
                    "Ошибка: неизвестная команда «" + args[0] + "». " +
                            "Введите help, чтобы увидеть список доступных команд."
            );
        }

        String rawArgs = (args.length > 1) ? args[1] : "";

        try {
            return command.execute(rawArgs);
        } catch (IllegalArgumentException | NullPointerException ex) {
            return List.of("Ошибка: " + ex.getMessage());
        }
    }

    private void register(Command command) {
        commands.put(command.getName().toLowerCase(), command);

        List<String> usage = command.getUsage();
        String usageValue = (usage.isEmpty()) ? "" : " " + String.join("\n  " + command.getName() + " ", usage);

        commandsInformation.add("  " + command.getName() + usageValue + "\n\t" + command.getDescription() + "\n");
    }
}
