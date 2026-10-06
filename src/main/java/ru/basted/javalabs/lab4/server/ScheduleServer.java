package ru.basted.javalabs.lab4.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import ru.basted.javalabs.lab4.common.Protocol;
import ru.basted.javalabs.lab4.server.commands.CommandManager;
import ru.basted.javalabs.lab4.server.net.ClientHandler;
import ru.basted.javalabs.lab4.server.schedules.ScheduleRepository;

public class ScheduleServer {
    private static final AtomicInteger THREAD_COUNTER = new AtomicInteger();
    private static final List<ClientHandler> CLIENT_HANDLERS = new CopyOnWriteArrayList<>();

    private static final ScheduleRepository SCHEDULE_REPOSITORY = new ScheduleRepository();
    private static final CommandManager COMMAND_MANAGER = new CommandManager(SCHEDULE_REPOSITORY);

    private static ServerSocket serverSocket;
    private static volatile boolean running = true;

    public static void main(String[] args) {
        System.out.println("[DEBUG] Сервер запускается на порту: " + Protocol.DEFAULT_PORT);

        Locale.setDefault(Locale.of("ru", "RU"));

        openConnection();
        registerShutdownHook();

        while (running) {
            acceptNewConnection();
        }
    }

    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[DEBUG] Получен SIGINT сигнал. Начинаю завершение работы.");

            running = false;
            closeConnections();

            System.out.println("[DEBUG] Все соединения закрыты. Сервер остановлен.");
        }));
    }

    private static void openConnection() {
        try {
            serverSocket = new ServerSocket(Protocol.DEFAULT_PORT);
        } catch (IOException ex) {
            System.err.println("[ERROR] Сервер не смог запуститься: " + ex.getMessage());
            System.exit(1);
        }
    }

    private static void acceptNewConnection() {
        try {
            Socket client = serverSocket.accept();

            ClientHandler clientHandler = new ClientHandler(
                    client,
                    COMMAND_MANAGER,
                    ScheduleServer::removeHandler
            );
            CLIENT_HANDLERS.add(clientHandler);

            Thread thread = new Thread(clientHandler, "client-" + THREAD_COUNTER.getAndIncrement());
            thread.start();
        } catch (IOException ex) {
            if (running) {
                System.err.println("[ERROR] Сервер не смог установить соединение с клиентом: " + ex.getMessage());
            }
        }
    }

    private static void removeHandler(ClientHandler handler) {
        CLIENT_HANDLERS.remove(handler);
    }

    private static void closeConnections() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            System.err.println("[ERROR] Критическая ошибка ОС при освобождении порта: " + ex.getMessage());
        }

        CLIENT_HANDLERS.forEach(clientHandler -> {
            try {
                clientHandler.sendMessage("[Server] Завершение работы. Соединение закрывается.");
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.err.println("[ERROR] " + ex.getMessage());
            }

            clientHandler.closeConnection();
        });
    }
}
