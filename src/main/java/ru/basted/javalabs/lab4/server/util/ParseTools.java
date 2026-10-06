package ru.basted.javalabs.lab4.server.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import ru.basted.javalabs.lab4.server.schedules.LessonSlot;
import ru.basted.javalabs.lab4.server.schedules.LessonType;

public final class ParseTools {
    private ParseTools() {
        throw new IllegalStateException("ParseTools — утилитный класс, создавать его экземпляры нельзя");
    }

    public static List<String> getFormatsInformation() {
        List<String> formatsInformation = new ArrayList<>();
        formatsInformation.add("Форматы параметров:");

        formatsInformation.add("  дата       ГГГГ-ММ-ДД, например 2026-09-01");
        formatsInformation.add("  номер_пары число от 1 до 7");
        formatsInformation.add("  тип        " + LessonType.getAllTypesName() + "\n");

        return formatsInformation;
    }

    public static LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date.trim());
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(
                    "Некорректная дата '" + date + "'. " +
                            "Ожидается существующая дата в формате ГГГГ-ММ-ДД (например, 2026-09-01)."
            );
        }
    }

    public static LessonSlot parseLessonSlot(String rawLessonNumber) {
        try {
            int lessonNumber = Integer.parseInt(rawLessonNumber.trim());
            return LessonSlot.fromId(lessonNumber);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Номер пары '" + rawLessonNumber + "' должен быть целым числом от 1 до "
                            + LessonSlot.values().length + "."
            );
        }
    }

    public static LessonType parseLessonType(String rawLessonType) {
        return LessonType.fromName(rawLessonType.trim());
    }
}
