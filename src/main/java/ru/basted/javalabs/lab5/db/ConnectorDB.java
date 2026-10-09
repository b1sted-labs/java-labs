package ru.basted.javalabs.lab5.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public final class ConnectorDB {
    private ConnectorDB() {
        throw new UnsupportedOperationException("ConnectorDB — утилитный класс, создавать его экземпляры нельзя");
    }

    public static Connection getConnection() throws SQLException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("ru.basted.javalabs.lab5.database");

        String url = resourceBundle.getString("db.url");
        String user = resourceBundle.getString("db.user");
        String pass = resourceBundle.getString("db.password");

        return DriverManager.getConnection(url, user, pass);
    }
}
