package ru.basted.javalabs.lab4.server.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

import ru.basted.javalabs.lab4.common.Protocol;

public class ClientHandler implements Runnable {
    private final Socket socket;
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

    public synchronized void sendMessage(String message) {
        if (message.isBlank()) {
            throw new IllegalArgumentException("Текст сообщения не может быть пустым или состоять только из пробелов");
        }

        if (socketWriter == null) {
            throw new IllegalStateException("Не удалось отправить сообщение: поток вывода (output) не инициализирован");
        }

        if (socket.isClosed()) {
            throw new IllegalStateException("Не удалось отправить сообщение: сетевой сокет уже закрыт");
        }

        socketWriter.println(message);
        socketWriter.println(Protocol.END_OF_RESPONSE);

        if (socketWriter.checkError()) {
            throw new IllegalStateException(
                    "Не удалось отправить сообщение клиенту " + socket.getRemoteSocketAddress()
                            + ": соединение разорвано или поток вывода поврежден");
        }
    }

    public void closeConnection() {
        if (onDisconnectAction != null) {
            onDisconnectAction.accept(this);
        }

        try {
            socket.close();
        } catch (IOException ex) {
            System.err.println("[ERROR] Ошибка при закрытии соединения с клиентом: " + ex.getMessage());
        }
    }
}
