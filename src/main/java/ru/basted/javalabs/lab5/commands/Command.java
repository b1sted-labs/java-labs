package ru.basted.javalabs.lab5.commands;

import java.sql.SQLException;
import java.util.List;

public interface Command {
    List<String> execute(String rawArgs) throws SQLException;

    String getName();
    String getDescription();
    List<String> getUsage();
}
