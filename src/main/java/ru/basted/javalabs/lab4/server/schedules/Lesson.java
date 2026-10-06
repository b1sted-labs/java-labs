package ru.basted.javalabs.lab4.server.schedules;

import java.util.Objects;

public record Lesson(
        String name,
        LessonType lessonType,
        String teacher,
        String room
) {
    public Lesson {
        Objects.requireNonNull(name, "Название пары не может быть null.");
        Objects.requireNonNull(teacher, "Имя преподавателя не может быть null.");
        Objects.requireNonNull(room, "Аудитория не может быть null.");

        if (name.isBlank()) {
            throw new IllegalArgumentException("Название пары не может быть пустым.");
        }

        if (teacher.isBlank()) {
            throw new IllegalArgumentException("Имя преподавателя не может быть пустым.");
        }

        if (room.isBlank()) {
            throw new IllegalArgumentException("Аудитория не может быть пустой.");
        }
    }

    @Override
    public String toString() {
        return "%s\n%s\n%s\n%s\n".formatted(name, lessonType, teacher, room);
    }
}
