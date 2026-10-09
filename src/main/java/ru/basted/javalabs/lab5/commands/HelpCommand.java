package ru.basted.javalabs.lab5.commands;

import java.util.List;

public class HelpCommand extends BaseCommand {
    private final List<String> commandsInformation;

    public HelpCommand(List<String> commandsInformation) {
        super("help", "Показать эту справку.", List.of());

        this.commandsInformation = commandsInformation;
    }

    @Override
    public List<String> execute(String rawArgs) {
        return commandsInformation;
    }
}
