package ru.basted.javalabs.lab4.client;

import java.io.IOException;
import java.net.Socket;

import ru.basted.javalabs.lab4.client.net.ServerListener;
import ru.basted.javalabs.lab4.client.net.ServerSender;
import ru.basted.javalabs.lab4.common.Log;
import ru.basted.javalabs.lab4.common.Protocol;

public class ServerClient {
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        Thread.currentThread().setName("ServerClient");

        try (Socket socket = new Socket(HOST, Protocol.DEFAULT_PORT)) {
            Log.info("Соединение установлено. Введите help, чтобы увидеть список команд, или exit для выхода.");

            registerShutdownHook(socket);

            startSenderThread(socket);

            Thread listenerThread = startListenerThread(socket);
            listenerThread.join();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            Log.error("Клиент был прерван во время ожидания ответов сервера: " + ex.getMessage());
        } catch (IOException ex) {
            Log.error(
                    "Не удалось подключиться к серверу " + HOST + ":" + Protocol.DEFAULT_PORT + ":"
                            + ex.getMessage() + ". Проверьте, что сервер запущен."
            );
            System.exit(1);
        }
    }

    private static void registerShutdownHook(Socket socket) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Log.info("Завершение работы клиента...");

            try {
                socket.close();
            } catch (IOException ex) {
                Log.error("Не удалось корректно закрыть соединение с сервером: " + ex.getMessage());
            }

            Log.info("Соединение с сервером закрыто.");
        }, "ShutdownHook"));
    }

    private static void startSenderThread(Socket socket) {
        ServerSender serverSender = new ServerSender(socket);

        Thread senderThread = new Thread(serverSender, "ServerSender");
        senderThread.setDaemon(true);
        senderThread.start();
    }

    private static Thread startListenerThread(Socket socket) {
        ServerListener serverListener = new ServerListener(socket);

        Thread listenerThread = new Thread(serverListener, "ServerListener");
        listenerThread.start();

        return listenerThread;
    }
}
