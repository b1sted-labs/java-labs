package ru.basted.javalabs.lab5.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public final class ParseTools {
    private ParseTools() {
        throw new UnsupportedOperationException("ParseTools — утилитный класс, создавать его экземпляры нельзя");
    }

    public static List<String> getFormatsInformation() {
        List<String> formatsInformation = new ArrayList<>();

        formatsInformation.add("Форматы параметров:\n");
        formatsInformation.add("  дата  ГГГГ-ММ-ДДTЧЧ:ММ:СС или ГГГГ-ММ-ДД ЧЧ:ММ:СС, например 2026-09-01T20:08:32\n");

        return formatsInformation;
    }

    public static LocalDateTime parseDateTime(String rawDateTime) {
        try {
            String cleaned = rawDateTime.trim().replace(" ", "T");

            return LocalDateTime.parse(cleaned);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(
                    "Некорректная дата '" + rawDateTime + "'. " +
                            "Ожидается существующая дата в формате ГГГГ-ММ-ДДTЧЧ:ММ:СС или ГГГГ-ММ-ДД ЧЧ:ММ:СС " +
                            "(например, 2026-09-01T20:08:32)."
            );
        }
    }

    public static int parseInteger(String rawNumber) {
        try {
            return Integer.parseInt(rawNumber.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Некорректное число '" + rawNumber + "'. " +
                            "Ожидается целое числовое значение (например, 123 или -45)."
            );
        }
    }
}
