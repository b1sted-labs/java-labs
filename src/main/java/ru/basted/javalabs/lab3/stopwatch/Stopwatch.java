package ru.basted.javalabs.lab3.stopwatch;

import java.util.concurrent.atomic.AtomicLong;

public class Stopwatch implements Runnable {
    private final long MEASUREMENT_INTERVAL = 50L;

    private volatile boolean isRunning;
    private volatile AtomicLong elapsedTime;

    private Thread thread;

    @Override
    public void run() {
        while (isRunning) {
            long currentTime = System.nanoTime();

            try {
                Thread.sleep(MEASUREMENT_INTERVAL);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            elapsedTime.addAndGet(System.nanoTime() - currentTime);
        }
    }

    public Stopwatch start(int identifier) {
        elapsedTime = new AtomicLong(0);
        isRunning = true;

        thread = new Thread(this, "stopwatch-" + identifier);
        thread.start();

        return this;
    }

    public void stop() throws InterruptedException {
        isRunning = false;
        thread.interrupt();
        thread.join();
    }

    public void reset() {
        elapsedTime = new AtomicLong(0);
    }

    public long showTime() {
        return elapsedTime.get();
    }

    public Thread getThread() {
        return thread;
    }
}
