package ru.basted.javalabs.lab5.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import ru.basted.javalabs.lab5.common.DBSchema;

public final class DBInitializer {
    private DBInitializer() {
        throw new UnsupportedOperationException("DBInitializer — утилитный класс, создавать его экземпляры нельзя");
    }

    public static void initialize(Connection connection) throws SQLException {
        createTable(connection);
        createTrigger(connection);
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTable = """
                CREATE TABLE IF NOT EXISTS %s (
                    %s INT AUTO_INCREMENT PRIMARY KEY,
                    %s INT NOT NULL UNIQUE,
                    %s INT NOT NULL,
                    %s DATETIME DEFAULT NOW()
                );
                """.formatted(DBSchema.TABLE_NAME, DBSchema.DRAW_NUMBER, DBSchema.TICKET_NUMBER, DBSchema.PRIZE_AMOUNT,
                DBSchema.DRAW_DATE);

        createAndExecuteStatement(connection, createTable);
    }

    private static void createTrigger(Connection connection) throws SQLException {
        String createTrigger = """
                CREATE TRIGGER IF NOT EXISTS %s
                BEFORE INSERT ON %s
                FOR EACH ROW
                BEGIN
                    IF EXISTS (SELECT 1 FROM %s WHERE %s > NEW.%s) THEN
                        SIGNAL SQLSTATE '45000'
                        SET MESSAGE_TEXT = 'Дата розыгрыша нового тиража не может быть раньше даты розыгрыша предыдущих тиражей';
                    END IF;
                END
                """.formatted(DBSchema.TRIGGER_NAME, DBSchema.TABLE_NAME, DBSchema.TABLE_NAME, DBSchema.DRAW_DATE,
                DBSchema.DRAW_DATE);

        createAndExecuteStatement(connection, createTrigger);
    }

    private static void createAndExecuteStatement(Connection connection, String toBeExecute) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(toBeExecute);
    }
}
