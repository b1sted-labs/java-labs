package ru.basted.javalabs.lab4.server.schedules;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class ScheduleRepository {
    private final Map<String, ScheduleOfGroup> groups = new ConcurrentSkipListMap<>();

    public List<String> getAllSchedule() {
        if (groups.isEmpty()) {
            return List.of("Расписание пока пусто. Добавьте первую пару командой add (формат: help).");
        }

        List<String> schedule = new ArrayList<>();
        groups.forEach((name, scheduleOfGroup) -> schedule.add(scheduleOfGroup.toString()));

        return schedule;
    }

    public synchronized ScheduleOfGroup getOrCreate(String name) {
        return groups.computeIfAbsent(name, groupSchedule -> new ScheduleOfGroup(name));
    }

    public ScheduleOfGroup findByName(String name) {
        return groups.get(name);
    }

    public synchronized String renameGroup(String oldName, String newName) {
        if (oldName.equals(newName)) {
            throw new IllegalArgumentException(
                    "Переименование невозможно: новое имя «" + newName + "» совпадает со старым."
            );
        }

        if (groups.containsKey(newName)) {
            throw new IllegalArgumentException("Переименование невозможно: группа «" + newName + "» уже существует.");
        }

        ScheduleOfGroup oldSchedule = groups.remove(oldName);
        if (oldSchedule == null) {
            throw new IllegalArgumentException("Переименование невозможно: группа «" + oldName + "» не найдена.");
        }

        ScheduleOfGroup newSchedule = new ScheduleOfGroup(newName, oldSchedule);
        groups.put(newName, newSchedule);

        return "Группа «" + oldName + "» переименована в «" + newName + "».";
    }

    public synchronized String deleteGroup(String name) {
        if (groups.remove(name) == null) {
            throw new IllegalArgumentException("Удаление невозможно: группа «" + name + "» не найдена.");
        }

        return "Группа «" + name + "» удалена вместе со всем расписанием.";
    }

    public synchronized String addLesson(
            String groupName,
            LocalDate date,
            LessonSlot lessonSlot,
            Lesson lesson
    ) {
        ScheduleOfGroup scheduleOfGroup = getOrCreate(groupName);
        scheduleOfGroup.addLesson(date, lessonSlot, lesson);

        return "Пара добавлена: группа «%s», %s, пара №%d — %s (%s), преподаватель: %s, аудитория: %s."
                .formatted(groupName, date, lessonSlot.getLessonNumber(),
                        lesson.name(), lesson.lessonType(), lesson.teacher(), lesson.room());
    }

    public synchronized String updateLesson(
            String groupName,
            LocalDate date,
            LessonSlot lessonSlot,
            Lesson lesson
    ) {
        ScheduleOfGroup scheduleOfGroup = findByName(groupName);
        if (scheduleOfGroup == null) {
            throw new IllegalArgumentException("Группа «" + groupName + "» не найдена.");
        }

        scheduleOfGroup.updateLesson(date, lessonSlot, lesson);

        return "Пара обновлена: группа «%s», %s, пара №%d — %s (%s), преподаватель: %s, аудитория: %s."
                .formatted(groupName, date, lessonSlot.getLessonNumber(),
                        lesson.name(), lesson.lessonType(), lesson.teacher(), lesson.room());
    }

    public synchronized String removeLesson(
            String groupName,
            LocalDate date,
            LessonSlot lessonSlot
    ) {
        ScheduleOfGroup scheduleOfGroup = findByName(groupName);
        if (scheduleOfGroup == null) {
            throw new IllegalArgumentException("Группа «" + groupName + "» не найдена.");
        }

        scheduleOfGroup.removeLesson(date, lessonSlot);
        String message = "Пара №%d на %s удалена из расписания группы «%s»."
                .formatted(lessonSlot.getLessonNumber(), date, groupName);

        if (hasNoRecords(scheduleOfGroup)) {
            deleteGroup(groupName);
            message += "\nВ группе не осталось пар, поэтому группа тоже удалена.";
        }

        return message;
    }

    public synchronized boolean hasNoRecords(String groupName) {
        ScheduleOfGroup scheduleOfGroup = findByName(groupName);
        if (scheduleOfGroup == null) {
            throw new IllegalArgumentException("Группа «" + groupName + "» не найдена.");
        }

        return scheduleOfGroup.hasNoRecords();
    }

    private synchronized boolean hasNoRecords(ScheduleOfGroup scheduleOfGroup) {
        return scheduleOfGroup.hasNoRecords();
    }
}
