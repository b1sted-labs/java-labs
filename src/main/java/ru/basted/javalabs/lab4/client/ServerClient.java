package ru.basted.javalabs.lab4.client;

import java.io.IOException;
import java.net.Socket;

import ru.basted.javalabs.lab4.client.net.ServerListener;
import ru.basted.javalabs.lab4.client.net.ServerSender;
import ru.basted.javalabs.lab4.common.Protocol;

public class ServerClient {
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        System.out.println("Подключение к серверу...");

        try (Socket socket = new Socket(HOST, Protocol.DEFAULT_PORT)) {
            System.out.println("[INFO] Подключение с сервером установлено.");

            registerShutdownHook(socket);

            startSenderThread(socket);

            Thread listenerThread = startListenerThread(socket);
            listenerThread.join();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            System.err.printf("[ERROR] Главный поток был прерван во время ожидания завершения ServerListener: %s%n",
                    ex.getMessage());
        } catch (IOException ex) {
            System.err.println("[ERROR] Клиент не смог установить соединение с сервером: " + ex.getMessage());
            System.exit(1);
        }
    }

    private static void registerShutdownHook(Socket socket) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[DEBUG] Запущен процесс завершения работы приложения. Начинаю очистку ресурсов.");

            try {
                socket.close();
            } catch (IOException ex) {
                System.err.println("[ERROR] Ошибка при закрытии соединения: " + ex.getMessage());
            }

            System.out.println("[DEBUG] Соединение с сервером разорвано. Клиент отключен.");
        }));
    }

    private static void startSenderThread(Socket socket) {
        ServerSender serverSender = new ServerSender(socket);

        Thread senderThread = new Thread(serverSender, "serversender");
        senderThread.setDaemon(true);
        senderThread.start();
    }

    private static Thread startListenerThread(Socket socket) {
        ServerListener serverListener = new ServerListener(socket);

        Thread listenerThread = new Thread(serverListener, "serverlistener");
        listenerThread.start();

        return listenerThread;
    }
}
