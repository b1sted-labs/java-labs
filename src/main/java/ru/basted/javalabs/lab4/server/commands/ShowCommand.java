package ru.basted.javalabs.lab4.server.commands;

import java.util.List;

import ru.basted.javalabs.lab4.server.schedules.ScheduleOfGroup;
import ru.basted.javalabs.lab4.server.schedules.ScheduleRepository;

public class ShowCommand extends BaseCommand {
    private final ScheduleRepository scheduleRepository;

    public ShowCommand(ScheduleRepository scheduleRepository) {
        super("show", "Показать расписание всех групп или только указанной.", List.of("[группа]"));

        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<String> execute(String rawArgs) {
        if (rawArgs.isBlank()) {
            return scheduleRepository.getAllSchedule();
        }

        ScheduleOfGroup scheduleOfGroup = scheduleRepository.findByName(rawArgs.trim());
        if (scheduleOfGroup == null) {
            throw new IllegalArgumentException(
                    "Группа «" + rawArgs.trim() + "» не найдена. Введите show без параметров, чтобы увидеть все группы."
            );
        }

        return List.of(scheduleOfGroup.toString());
    }
}
