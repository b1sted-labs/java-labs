package ru.basted.javalabs.lab4.server.schedules;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class ScheduleOfGroup {
    private final String name;
    private final Map<LocalDate, Map<LessonSlot, Lesson>> schedule;

    public ScheduleOfGroup(String name) {
        this.name = name;
        this.schedule = new ConcurrentSkipListMap<>();
    }

    public ScheduleOfGroup(String name, ScheduleOfGroup other) {
        this.name = name;
        this.schedule = new ConcurrentSkipListMap<>(other.schedule);
    }

    public synchronized void addLesson(LocalDate date, LessonSlot lessonSlot, Lesson lesson) {
        Map<LessonSlot, Lesson> lessonInSchedule = schedule.computeIfAbsent(date, d -> new EnumMap<>(LessonSlot.class));

        if (lessonInSchedule.containsKey(lessonSlot)) {
            throw new IllegalArgumentException(
                    "В слоте №%d на %s уже есть пара. Чтобы заменить её, используйте update, чтобы убрать — remove."
                            .formatted(lessonSlot.getLessonNumber(), date)
            );
        }

        lessonInSchedule.put(lessonSlot, lesson);
    }

    public synchronized void updateLesson(LocalDate date, LessonSlot lessonSlot, Lesson newLesson) {
        Map<LessonSlot, Lesson> scheduleOfDay = schedule.get(date);
        if (scheduleOfDay == null) {
            throw new IllegalArgumentException("Изменение невозможно: на %s у группы нет занятий.".formatted(date));
        }

        if (scheduleOfDay.remove(lessonSlot) == null) {
            throw new IllegalArgumentException(
                    "Изменение невозможно: в слоте №%d на %s нет пары. Чтобы добавить её, используйте add."
                            .formatted(lessonSlot.getLessonNumber(), date)
            );
        }

        scheduleOfDay.put(lessonSlot, newLesson);
    }

    public synchronized void removeLesson(LocalDate date, LessonSlot lessonSlot) {
        Map<LessonSlot, Lesson> scheduleOfDay = schedule.get(date);
        if (scheduleOfDay == null) {
            throw new IllegalArgumentException("Удаление невозможно: на %s у группы нет занятий.".formatted(date));
        }

        if (scheduleOfDay.remove(lessonSlot) == null) {
            throw new IllegalArgumentException(
                    "Удаление невозможно: в слоте №%d на %s нет пары.".formatted(lessonSlot.getLessonNumber(), date)
            );
        }

        if (scheduleOfDay.isEmpty()) {
            schedule.remove(date);
        }
    }

    public synchronized boolean hasNoRecords() {
        return schedule.isEmpty();
    }

    @Override
    public synchronized String toString() {
        StringBuilder scheduleString = new StringBuilder("Группа «" + name + "»\n\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy");

        schedule.forEach((localDate, lessons) -> {
            String date = localDate.format(formatter);

            scheduleString.append(date.substring(0, 1).toUpperCase())
                    .append(date.substring(1))
                    .append("\n\n");

            lessons.forEach((lessonSlot, lesson) -> {
                scheduleString.append(lessonSlot)
                        .append(". ")
                        .append(lesson)
                        .append("\n");
            });
        });

        scheduleString.deleteCharAt(scheduleString.length() - 1);

        return scheduleString.toString();
    }
}
