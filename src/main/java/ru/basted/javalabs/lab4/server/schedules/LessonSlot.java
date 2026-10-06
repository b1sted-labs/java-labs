package ru.basted.javalabs.lab4.server.schedules;

import java.time.LocalTime;

public enum LessonSlot {
    FIRST_SLOT(1, LocalTime.of(9, 0), LocalTime.of(10, 35)),
    SECOND_SLOT(2, LocalTime.of(10, 45), LocalTime.of(12, 20)),
    THIRD_SLOT(3, LocalTime.of(13, 0), LocalTime.of(14, 35)),
    FOURTH_SLOT(4, LocalTime.of(14, 45), LocalTime.of(16, 20)),
    FIFTH_SLOT(5, LocalTime.of(16, 30), LocalTime.of(18, 5)),
    SIXTH_SLOT(6, LocalTime.of(18, 15), LocalTime.of(19, 50)),
    SEVENTH_SLOT(7, LocalTime.of(20, 0), LocalTime.of(21, 35));

    private final int lessonNumber;
    private final LocalTime startTime;
    private final LocalTime endTime;

    LessonSlot(int lessonNumber, LocalTime startTime, LocalTime endTime) {
        this.lessonNumber = lessonNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static LessonSlot fromId(int lessonNumber) {
        for (LessonSlot slot : values()) {
            if (slot.lessonNumber == lessonNumber) {
                return slot;
            }
        }

        throw new IllegalArgumentException(
                "Неизвестный номер пары: " + lessonNumber + ". Допустимы значения от 1 до " + values().length + "."
        );
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    @Override
    public String toString() {
        return "%s-%s\n%s".formatted(startTime, endTime, lessonNumber);
    }
}
