package ru.basted.javalabs.lab2.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TextReader {
    public static ArrayList<String> readDataFromFileUsingBufferedReader(String filePath) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            ArrayList<String> readLines = new ArrayList<>();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                readLines.add(line);
            }

            return readLines;
        }
    }

    public static ArrayList<String> readDataFromFileUsingFile(String filePath) throws IOException  {
        List<String> readLines = Files.readAllLines(Path.of(filePath));
        return new ArrayList<>(readLines);
    }
}
