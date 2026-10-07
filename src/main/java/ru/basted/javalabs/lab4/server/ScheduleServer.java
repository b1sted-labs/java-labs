package ru.basted.javalabs.lab4.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import ru.basted.javalabs.lab4.common.Log;
import ru.basted.javalabs.lab4.common.Protocol;
import ru.basted.javalabs.lab4.server.commands.CommandManager;
import ru.basted.javalabs.lab4.server.net.ClientHandler;
import ru.basted.javalabs.lab4.server.schedules.ScheduleRepository;

public class ScheduleServer {
    private static final List<ClientHandler> CLIENT_HANDLERS = new CopyOnWriteArrayList<>();

    private static final ScheduleRepository SCHEDULE_REPOSITORY = new ScheduleRepository();
    private static final CommandManager COMMAND_MANAGER = new CommandManager(SCHEDULE_REPOSITORY);

    private static ServerSocket serverSocket;
    private static volatile boolean running = true;

    public static void main(String[] args) {
        Locale.setDefault(Locale.of("ru", "RU"));
        Thread.currentThread().setName("ScheduleServer");

        openConnection();
        registerShutdownHook();

        while (running) {
            acceptNewConnection();
        }
    }

    private static void openConnection() {
        try {
            serverSocket = new ServerSocket(Protocol.DEFAULT_PORT);
            Log.info("Сервер запущен и ожидает подключений на порту " + Protocol.DEFAULT_PORT);
        } catch (IOException ex) {
            Log.error(
                    "Не удалось запустить сервер на порту " + Protocol.DEFAULT_PORT + ": " + ex.getMessage()
                            + ". Возможно, порт уже занят другим приложением."
            );
            System.exit(1);
        }
    }

    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Log.info("Получен сигнал завершения (Ctrl+C / SIGTERM). Останавливаю сервер...");

            running = false;
            closeConnections();

            Log.info("Все клиентские соединения закрыты. Сервер остановлен.");
        }, "ShutdownHook"));
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

            Thread thread = new Thread(
                    clientHandler,
                    "ClientHandler-" + String.valueOf(client.getRemoteSocketAddress()).substring(1)
            );
            thread.start();
        } catch (IOException ex) {
            if (running) {
                Log.error("Не удалось принять входящее подключение: " + ex.getMessage());
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
            Log.error("Не удалось закрыть серверный сокет (порт " + Protocol.DEFAULT_PORT + "): " + ex.getMessage());
        }

        CLIENT_HANDLERS.forEach(clientHandler -> {
            try {
                clientHandler.sendMessage("Завершение работы. Соединение закрывается.");
            } catch (IllegalArgumentException | IllegalStateException ex) {
                Log.warn("Не удалось уведомить клиента о завершении работы: " + ex.getMessage());
            }

            clientHandler.closeConnection();
        });
    }
}
