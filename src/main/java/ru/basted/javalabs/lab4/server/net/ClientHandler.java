package ru.basted.javalabs.lab4.server.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import ru.basted.javalabs.lab4.common.Log;
import ru.basted.javalabs.lab4.common.Protocol;
import ru.basted.javalabs.lab4.server.commands.CommandManager;

public class ClientHandler implements Runnable {
    private final AtomicBoolean closed = new AtomicBoolean(false);

    private final Socket socket;
    private final Consumer<ClientHandler> onDisconnectAction;

    private final CommandManager commandManager;

    private volatile PrintWriter socketWriter;
    private volatile BufferedReader socketReader;

    public ClientHandler(
            Socket socket,
            CommandManager commandManager,
            Consumer<ClientHandler> onDisconnectAction
    ) {
        this.socket = socket;
        this.commandManager = commandManager;
        this.onDisconnectAction = onDisconnectAction;
    }

    @Override
    public void run() {
        String address = String.valueOf(socket.getRemoteSocketAddress()).substring(1);
        Log.info("Клиент подключился: " + address);

        try {
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Protocol.CHARSET));
            socketWriter = new PrintWriter(socket.getOutputStream(), true, Protocol.CHARSET);

            while (true) {
                String command = getMessage();
                if (command == null) {
                    break;
                }

                if (command.isBlank()) {
                    sendMessage("");
                    continue;
                }

                String firstWord = command.trim().split("\\s+", 2)[0];
                if (firstWord.equalsIgnoreCase(Protocol.EXIT_COMMAND)) {
                    break;
                }

                if (command.contains(Protocol.END_OF_RESPONSE)) {
                    Log.warn("Клиент отправил ввод со служебной последовательностью протокола, команда отклонена");

                    sendMessage(
                            "Ошибка: Ввод содержит зарезервированную служебную последовательность '"
                                    + Protocol.END_OF_RESPONSE + "'. Удалите её и повторите команду."
                    );
                    continue;
                }

                try {
                    sendMessages(commandManager.execute(command));
                    Log.debug(
                            (command.length() > 199)
                                    ? "Получена команда: " + command.substring(0, 199) + "..."
                                    : "Получена команда: " + command
                    );
                } catch (IllegalArgumentException | IllegalStateException ex) {
                    Log.error("Не удалось обработать запрос клиента: " + ex.getMessage() + ". Соединение будет закрыто.");
                    break;
                }
            }
        } catch (IOException ex) {
            if (!socket.isClosed()) {
                Log.error("Ошибка ввода-вывода при обмене данными с клиентом: " + ex.getMessage());
            }
        } finally {
            closeConnection();
            Log.info("Клиент отключился: " + address);
        }
    }

    public String getMessage() throws IOException {
        return socketReader.readLine();
    }

    public void sendMessage(String message) throws IllegalArgumentException, IllegalStateException {
        sendMessages(List.of(message));
    }

    public synchronized void sendMessages(List<String> messages) {
        if (socketWriter == null) {
            throw new IllegalStateException(
                    "Не удалось отправить ответ клиенту: поток вывода ещё не инициализирован"
            );
        }

        if (socket.isClosed()) {
            throw new IllegalStateException(
                    "Не удалось отправить ответ клиенту: соединение уже закрыто"
            );
        }

        validateMessages(messages);
        messages.forEach(message -> {
            if (message.isBlank()) {
                return;
            }

            socketWriter.println(message);
        });
        socketWriter.println(Protocol.END_OF_RESPONSE);

        if (socketWriter.checkError()) {
            throw new IllegalStateException(
                    "Не удалось доставить ответ клиенту: соединение разорвано или поток вывода поврежден"
            );
        }
    }

    public void closeConnection() {
        if (!closed.compareAndSet(false, true)) {
            return;
        }

        if (onDisconnectAction != null) {
            onDisconnectAction.accept(this);
        }

        try {
            socket.close();
        } catch (IOException ex) {
            Log.error("Не удалось корректно закрыть соединение с клиентом: " + ex.getMessage());
        }
    }

    private void validateMessages(List<String> messages) {
        if (messages == null) {
            throw new IllegalArgumentException("Нельзя отправить ответ клиенту: список сообщений равен null");
        }

        if (messages.isEmpty()) {
            throw new IllegalArgumentException("Нельзя отправить ответ клиенту: список сообщений пуст");
        }
    }
}
