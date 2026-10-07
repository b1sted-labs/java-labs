package ru.basted.javalabs.lab4.server.commands;

import java.util.List;

public interface Command {
    List<String> execute(String rawArgs);

    String getName();
    String getDescription();
    List<String> getUsage();
}
