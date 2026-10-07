package ru.basted.javalabs.lab4.server.commands;

import java.time.LocalDate;
import java.util.List;

import ru.basted.javalabs.lab4.server.schedules.Lesson;
import ru.basted.javalabs.lab4.server.schedules.LessonSlot;
import ru.basted.javalabs.lab4.server.schedules.LessonType;
import ru.basted.javalabs.lab4.server.schedules.ScheduleRepository;
import ru.basted.javalabs.lab4.server.util.ParseTools;

public class AddCommand extends BaseCommand {
    private final ScheduleRepository scheduleRepository;

    public AddCommand(ScheduleRepository scheduleRepository) {
        super(
                "add",
                "Добавить пару в расписание группы (группа создаётся автоматически).",
                List.of("<группа>;<дата>;<номер_пары>;<название>;<тип>;<преподаватель>;<аудитория>")
        );

        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<String> execute(String rawArgs) {
        String[] args = rawArgs.trim().split(";");
        if (args.length != 7) {
            throw new IllegalArgumentException(
                    "Неверное количество параметров. Ожидается 7 параметров, разделённых символом ';'." +
                            "\nИспользование: " + getName() + " " + getUsage().getFirst()
            );
        }

        if (args[0].isBlank()) {
            throw new IllegalArgumentException("Название группы не может быть пустым.");
        }

        LocalDate date = ParseTools.parseDate(args[1]);
        LessonSlot lessonSlot = ParseTools.parseLessonSlot(args[2]);
        LessonType lessonType = ParseTools.parseLessonType(args[4]);

        Lesson lesson = new Lesson(args[3].trim(), lessonType, args[5].trim(), args[6].trim());

        return List.of(scheduleRepository.addLesson(args[0].trim(), date, lessonSlot, lesson));
    }
}
