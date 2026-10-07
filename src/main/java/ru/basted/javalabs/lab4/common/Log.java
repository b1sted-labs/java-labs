package ru.basted.javalabs.lab4.common;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class Log {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private Log() {
        throw new UnsupportedOperationException("Log — утилитный класс, создавать его экземпляры нельзя");
    }

    public static void log(LogLevel logLevel, String message) {
        Thread thread = Thread.currentThread();
        String time = ZonedDateTime.now().format(formatter);

        String logString = "%s [%s] [%s] %s".formatted(time, logLevel.name(), thread.getName(), message);

        if (logLevel.isToStderr()) {
            System.err.println(logString);
        } else {
            System.out.println(logString);
        }
    }

    public static void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public static void info(String message) {
        log(LogLevel.INFO, message);
    }

    public static void warn(String message) {
        log(LogLevel.WARN, message);
    }

    public static void error(String message) {
        log(LogLevel.ERROR, message);
    }
}
