package ru.basted.javalabs.lab4.server.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import ru.basted.javalabs.lab4.common.Protocol;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final Consumer<ClientHandler> onDisconnectAction;

    private volatile PrintWriter socketWriter;
    private volatile BufferedReader socketReader;

    public ClientHandler(Socket socket, Consumer<ClientHandler> onDisconnectAction) {
        this.socket = socket;
        this.onDisconnectAction = onDisconnectAction;
    }

    @Override
    public void run() {
        System.out.printf("[DEBUG] Новый клиент установил соединение с сервером: %s%n", socket.getRemoteSocketAddress());

        try {
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Protocol.CHARSET));
            socketWriter = new PrintWriter(socket.getOutputStream(), true, Protocol.CHARSET);

            while (true) {
                String command = getMessage();
                if (command == null || command.equals(Protocol.EXIT_COMMAND)) {
                    break;
                }

                System.out.printf("[DEBUG] Пришла команда от %s: %s%n", socket.getRemoteSocketAddress(), command);

                try {
                    sendMessage("[Server] Команда " + command + " получена!");
                } catch (IllegalArgumentException ex) {
                    System.err.println("[ERROR] " + ex.getMessage());
                } catch (IllegalStateException ex) {
                    System.err.println("[ERROR] " + ex.getMessage());
                    break;
                }
            }
        } catch (IOException ex) {
            if (!socket.isClosed()) {
                System.err.println("[ERROR] Ошибка при подключении: " + ex.getMessage());
            }
        } finally {
            closeConnection();
            System.out.printf("[INFO] Клиент отключился: %s%n", socket.getRemoteSocketAddress());
        }
    }

    public String getMessage() throws IOException {
        return socketReader.readLine();
    }

    public synchronized void sendMessage(String message) throws IllegalArgumentException, IllegalStateException {
        sendMessages(List.of(message));
    }

    public synchronized void sendMessages(List<String> messages) {
        if (socketWriter == null) {
            throw new IllegalStateException("Не удалось отправить сообщения: поток вывода (output) не инициализирован");
        }

        if (socket.isClosed()) {
            throw new IllegalStateException("Не удалось отправить сообщения: сетевой сокет уже закрыт");
        }

        validateMessages(messages);
        messages.forEach(message -> socketWriter.println(message));
        socketWriter.println(Protocol.END_OF_RESPONSE);

        if (socketWriter.checkError()) {
            throw new IllegalStateException(
                    "Не удалось отправить ответ клиенту " + socket.getRemoteSocketAddress()
                            + ": соединение разорвано или поток вывода поврежден"
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
            System.err.println("[ERROR] Ошибка при закрытии соединения с клиентом: " + ex.getMessage());
        }
    }

    private void validateMessages(List<String> messages) {
        if (messages == null) {
            throw new IllegalArgumentException("Список сообщений не может быть null");
        }

        if (messages.isEmpty()) {
            throw new IllegalArgumentException("Список сообщений не может быть пустым");
        }

        for (int i = 0; i < messages.size(); i++) {
            String message = messages.get(i);
            if (message == null || message.isBlank()) {
                Optional<String> callerName = StackWalker.getInstance()
                        .walk(stackFrameStream -> stackFrameStream
                                .skip(1)
                                .dropWhile(frame -> frame.getMethodName().matches("^sendMessage(s)?$"))
                                .findFirst()
                                .map(StackWalker.StackFrame::getMethodName)
                        );

                throw new IllegalArgumentException(callerName + ": Сообщение с индексом " + i + " пустое или состоит только из пробелов");
            }
        }
    }
}
