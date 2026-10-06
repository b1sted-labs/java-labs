package ru.basted.javalabs.lab4.server.commands;

import java.time.LocalDate;
import java.util.List;

import ru.basted.javalabs.lab4.server.schedules.LessonSlot;
import ru.basted.javalabs.lab4.server.schedules.ScheduleRepository;
import ru.basted.javalabs.lab4.server.util.ParseTools;

public class RemoveCommand extends BaseCommand {
    private final ScheduleRepository scheduleRepository;

    public RemoveCommand(ScheduleRepository scheduleRepository) {
        super(
                "remove",
                "Удалить группу целиком или одну пару из её расписания.",
                List.of("<группа>", "<группа>;<дата>;<номер_пары>")
        );

        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<String> execute(String rawArgs) {
        String[] args = rawArgs.trim().split(";");
        if (args.length != 1 && args.length != 3) {
            String name = getName();
            List<String> usage = getUsage();

            throw new IllegalArgumentException(
                    "Неверное количество параметров.\nИспользование: %s %s\n%15s%s %s"
                            .formatted(name, usage.getFirst(), " ", name, usage.getLast())
            );
        }

        if (args[0].isBlank()) {
            throw new IllegalArgumentException("Название группы не может быть пустым.");
        }

        if (args.length == 1) {
            return List.of(scheduleRepository.deleteGroup(args[0].trim()));
        }

        LocalDate date = ParseTools.parseDate(args[1]);
        LessonSlot lessonSlot = ParseTools.parseLessonSlot(args[2]);

        return List.of(scheduleRepository.removeLesson(args[0].trim(), date, lessonSlot));
    }
}
