package ru.basted.javalabs.lab3.cli;

import ru.basted.javalabs.lab3.stopwatch.StopwatchManager;

import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Handler {
    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_RESET = "\u001B[0m";

    private static final Map<String, String> HELP_INFORMATION = new LinkedHashMap<>();

    static {
        HELP_INFORMATION.put("start {intIdentifier}", "Запустить секундомер и дать ему идентификатор N");
        HELP_INFORMATION.put("stop {intIdentifier}",  "Остановить секундомер с идентификатором N");
        HELP_INFORMATION.put("reset {intIdentifier}", "Сбросить время у секундомера с идентификатором N");
        HELP_INFORMATION.put("time {intIdentifier}", "Показать время у секундомера с идентификатором N");
        HELP_INFORMATION.put("help", "Показать список команд");
        HELP_INFORMATION.put("exit", "Выход");
    }

    private static boolean isRunning = true;

    public static boolean getIsRunning() {
        return isRunning;
    }

    public static void dispatch(
            Scanner scan,
            StopwatchManager stopwatchManager
    ) throws InputMismatchException, IllegalArgumentException, InterruptedException {
        System.out.print("\nВведите команду для продолжения: ");
        String command = scan.next().toLowerCase();

        switch (command) {
            case "start" -> {
                int identifier = scan.nextInt();
                stopwatchManager.startNewStopwatch(identifier);
            }

            case "stop" -> {
                int identifier = scan.nextInt();
                stopwatchManager.stopStopWatch(identifier);
            }

            case "reset" -> {
                int identifier = scan.nextInt();
                stopwatchManager.resetStopwatch(identifier);
            }

            case "time" -> {
                int identifier = scan.nextInt();
                stopwatchManager.showTime(identifier);
            }

            case "help" -> printHelp();

            case "exit" -> {
                stopwatchManager
                        .getActiveThreads()
                        .values()
                        .forEach(stopwatch -> {
                            try {
                                stopwatch.stop();
                            } catch (InterruptedException e) {
                                System.out.println("Поток " + stopwatch.getThread().getName() + " был прерван, пока он ждал worker!");
                            }
                        });
                isRunning = false;
            }

            default -> throw new IllegalArgumentException(
                    "Введенной команды не существует. Воспользуйтесь help для просмотра существующих команд."
            );
        }
    }

    public static void printHelp() {
        System.out.println("Консольный секундомер, написанный на Java/Thread");
        System.out.println();

        HELP_INFORMATION.forEach(Handler::printInformationAboutCommand);
    }

    private static void printInformationAboutCommand(String commandName, String information) {
        System.out.println(ANSI_BOLD + commandName + ANSI_RESET + " " + information);
    }
}
