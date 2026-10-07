package ru.basted.javalabs.lab4.server.commands;

import java.util.List;

import ru.basted.javalabs.lab4.server.schedules.ScheduleRepository;

public class RenameCommand extends BaseCommand {
    private final ScheduleRepository scheduleRepository;

    public RenameCommand(ScheduleRepository scheduleRepository) {
        super("rename", "Переименовать группу.", List.of("<старое_имя>;<новое_имя>"));

        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<String> execute(String rawArgs) {
        String[] args = rawArgs.trim().split(";");
        if (args.length != 2) {
            throw new IllegalArgumentException(
                    "Неверное количество параметров. Ожидается 2 параметра, разделённых символом ';'." +
                            "\nИспользование: " + getName() + " " + getUsage().getFirst()
            );
        }

        if (args[0].isBlank() || args[1].isBlank()) {
            throw new IllegalArgumentException("Название группы не может быть пустым");
        }

        return List.of(scheduleRepository.renameGroup(args[0].trim(), args[1].trim()));
    }
}
