package ru.basted.javalabs.lab2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import ru.basted.javalabs.lab2.file.TextReader;

public class Main {
    private static final String INPUT_FILE_PATH = "src/main/resources/ru/basted/javalabs/lab2/example_data.txt";
    private static final int SYSTEM_EXIT_FAILURE = 1;

    public static void main(String[] args) {
        try {
            ArrayList<String> readLines = TextReader.readDataFromFileUsingFile(INPUT_FILE_PATH);

            System.out.println("Исходный массив:");
            arrayOutput(readLines);
            System.out.println();

            Collections.sort(readLines);

            System.out.println("Массив после сортировки:");
            arrayOutput(readLines);
        } catch (IOException ex) {
            System.out.println("Произошла ошибка при чтении файла! " + ex.getMessage());
            System.exit(SYSTEM_EXIT_FAILURE);
        }
    }

    private static void arrayOutput(ArrayList<String> list) {
        for (String str : list) {
            System.out.println(str);
        }
    }
}
