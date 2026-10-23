package ru.basted.javalabs.lab7.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public final class DBConnector {
    private static HikariDataSource dataSource;

    private DBConnector() {
        throw new UnsupportedOperationException("ConnectorDB — утилитный класс, создавать его экземпляры нельзя");
    }

    public static void initializeDataSource() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("ru.basted.javalabs.lab5.database");

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(resourceBundle.getString("db.url"));
        config.setUsername(resourceBundle.getString("db.user"));
        config.setPassword(resourceBundle.getString("db.password"));

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(300_000);      // 300 000 ms = 5 min
        config.setConnectionTimeout(20_000); // 20 000 ms = 20 sec

        dataSource = new HikariDataSource(config);
        System.out.println("DBConnector: Пул соединений HikariCP успешно запущен.");
    }

    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
