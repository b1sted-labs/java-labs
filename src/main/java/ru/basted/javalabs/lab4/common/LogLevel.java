package ru.basted.javalabs.lab4.common;

public enum LogLevel {
    DEBUG(false),
    INFO(false),
    WARN(true),
    ERROR(true);

    private final boolean toStderr;

    LogLevel(boolean toStderr) {
        this.toStderr = toStderr;
    }

    public boolean isToStderr() {
        return toStderr;
    }
}
