package ru.basted.javalabs.lab3;

import ru.basted.javalabs.lab3.cli.Handler;
import ru.basted.javalabs.lab3.stopwatch.StopwatchManager;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        StopwatchManager stopwatchManager = new StopwatchManager();

        Handler.printHelp();

        while (Handler.getIsRunning()) {
            try {
                Handler.dispatch(scan, stopwatchManager);
            } catch (InputMismatchException ex) {
                System.out.println("Неверный ввод: введено не число. Попробуйте снова.");
                scan.nextLine();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        scan.close();
    }
}
