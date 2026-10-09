package ru.basted.javalabs.lab5.commands;

import java.util.List;
import java.util.Objects;

public abstract class BaseCommand implements Command {
    private final String name;
    private final String description;
    private final List<String> usage;

    protected BaseCommand(String name, String description, List<String> usage) {
        this.name = Objects.requireNonNull(name, "Имя команды не может быть null.");
        this.description = Objects.requireNonNull(description, "Описание команды не может быть null.");
        this.usage = Objects.requireNonNull(usage, "Использование команды не может быть null.");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<String> getUsage() {
        return usage;
    }
}
