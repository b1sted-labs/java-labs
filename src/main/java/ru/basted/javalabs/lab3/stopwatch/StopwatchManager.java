package ru.basted.javalabs.lab3.stopwatch;

import java.util.LinkedHashMap;
import java.util.Map;

public class StopwatchManager {
    private final Map<Integer, Stopwatch> activeThreads = new LinkedHashMap<>();

    public void startNewStopwatch(int identifier) throws IllegalArgumentException {
        if (identifier < 0) {
            throw new IllegalArgumentException("Не удалось создать поток: идентификатор меньше нуля.");
        }

        if (activeThreads.containsKey(identifier) && activeThreads.get(identifier).getThread().isAlive()) {
            throw new IllegalArgumentException("Не удалось создать поток: идентификатор уже занят.");
        }

        Stopwatch stopwatch = new Stopwatch().start(identifier);
        activeThreads.put(identifier, stopwatch);
        System.out.println("Секундомер в потоке " + identifier + " был запущен.");
    }

    public void stopStopWatch(int identifier) throws IllegalArgumentException, InterruptedException {
        if (!activeThreads.containsKey(identifier)) {
            throw new IllegalArgumentException("Потока №" + identifier + " не существует!");
        }

        if (!activeThreads.get(identifier).getThread().isAlive()) {
            throw new IllegalArgumentException("Поток №" + identifier + " уже остановлен!");
        }

        activeThreads.get(identifier).stop();
        System.out.println("Секундомер в потоке " + identifier + " был остановлен.");
    }

    public void resetStopwatch(int identifier) throws IllegalArgumentException {
        if (!activeThreads.containsKey(identifier)) {
            throw new IllegalArgumentException("Потока №" + identifier + " не существует!");
        }

        activeThreads.get(identifier).reset();
        System.out.println("Время секундомера в потоке №" + identifier + " было успешно сброшено.");
    }

    public void showTime(int identifier) throws IllegalArgumentException {
        if (!activeThreads.containsKey(identifier)) {
            throw new IllegalArgumentException("Потока №" + identifier + " не существует!");
        }

        System.out.printf(
                "Отсчитанное время в секундомере из потока %d: %d мс%n",
                identifier, activeThreads.get(identifier).showTime() / 1_000_000
        );
    }

    public Map<Integer, Stopwatch> getActiveThreads() {
        return activeThreads;
    }
}
