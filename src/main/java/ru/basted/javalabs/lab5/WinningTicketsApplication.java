package ru.basted.javalabs.lab5;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import ru.basted.javalabs.lab5.commands.CommandManager;
import ru.basted.javalabs.lab5.common.DBSchema;
import ru.basted.javalabs.lab5.db.ConnectorDB;
import ru.basted.javalabs.lab5.db.DBInitializer;

public class WinningTicketsApplication {
    public static void main(String[] args) {
        Locale.setDefault(Locale.of("ru", "RU"));
        Thread.currentThread().setName("WinningTicketsApplication");

        try (Connection connection = openConnection();
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Установлено подключение к базе данных: " + connection.getCatalog() + "\n");

            CommandManager commandManager = new CommandManager(connection);

            while (true) {
                System.out.print("# ");
                String command = scanner.nextLine();

                if (command.isBlank()) {
                    continue;
                }

                String firstWord = command.trim().split("\\s+", 2)[0];
                if (firstWord.equalsIgnoreCase("exit")) {
                    break;
                }

                try {
                    consolePrint(commandManager.execute(command));
                } catch (SQLException ex) {
                    if (!isAlive(connection)) {
                        System.out.println("Критическая ошибка: соединение с базой данных потеряно.");
                        break;
                    }

                    printCommandError(ex);
                } catch (IllegalArgumentException ex) {
                    printCommandError(ex);
                }
            }
        } catch (SQLException e) {
            System.err.println("Не удалось работать с базой данных: " + e.getMessage());
            System.exit(1);
        }
    }

    private static Connection openConnection() throws SQLException{
        Connection connection = ConnectorDB.getConnection();
        DBInitializer.initialize(connection);
        return connection;
    }

    private static void consolePrint(List<String> results) {
        for (String result : results) {
            System.out.print(result);
        }
    }

    private static boolean isAlive(Connection connection) {
        try {
            return connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    private static void printCommandError(Exception ex) {
        System.out.println("Ошибка при выполнении команды: " + ex.getMessage());
    }
}
