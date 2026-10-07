package ru.basted.javalabs.lab4.server.schedules;

import java.util.ArrayList;
import java.util.List;

public enum LessonType {
    LECTURE("Лекция"),
    PRACTICE("Практика"),
    LABORATORY("Лабораторная"),
    CONSULTATION("Консультация"),
    CREDIT("Зачёт"),
    EXAM("Экзамен");

    private final String name;

    LessonType(String name) {
        this.name = name;
    }

    public static LessonType fromName(String name) {
        String normalizedName = name.replace('ё', 'е').replace('Ё', 'Е');

        for (LessonType type : values()) {
            String typeName = type.name.replace('ё', 'е').replace('Ё', 'Е');

            if (typeName.equalsIgnoreCase(normalizedName)) {
                return type;
            }
        }

        throw new IllegalArgumentException(
                "Неизвестный тип занятия '" + name + "'. Допустимые значения: " + getAllTypesName() + "."
        );
    }

    public static String getAllTypesName() {
        List<String> types = new ArrayList<>();

        for (LessonType type : values()) {
            types.add(type.toString());
        }

        return String.join(", ", types);
    }

    @Override
    public String toString() {
        return name;
    }
}
