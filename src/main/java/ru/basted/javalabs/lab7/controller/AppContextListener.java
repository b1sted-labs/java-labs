package ru.basted.javalabs.lab7.controller;

import java.sql.SQLException;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import ru.basted.javalabs.lab7.db.DBConnector;
import ru.basted.javalabs.lab7.db.DBInitializer;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Веб-приложение запускается...");

        try {
            DBConnector.initializeDataSource();
            DBInitializer.initialize(DBConnector.getConnection());
        } catch (SQLException ex) {
            sce.getServletContext().log("Критическая ошибка при старте контекста", ex);
            throw new RuntimeException("Запуск невозможен: нет связи с БД", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DBConnector.closeDataSource();
        sce.getServletContext().log("Пул соединений HikariCP успешно закрыт.");
        System.out.println("Веб-приложение остановлено.");
    }
}
