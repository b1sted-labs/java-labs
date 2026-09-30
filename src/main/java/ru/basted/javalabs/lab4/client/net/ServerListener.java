package ru.basted.javalabs.lab4.client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import ru.basted.javalabs.lab4.common.Protocol;

public class ServerListener implements Runnable {
    private final String PROMPT = "# ";

    private final Socket socket;

    public ServerListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader socketReader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), Protocol.CHARSET)
        )) {
            System.out.print(PROMPT);

            while (true) {
                String message = socketReader.readLine();
                if (message == null) {
                    System.out.println("[INFO] Сервер закрыл соединение.");
                    break;
                }

                if (message.equals(Protocol.END_OF_RESPONSE)) {
                    System.out.print(PROMPT);
                    continue;
                }

                System.out.println(message);
            }
        } catch (IOException ex) {
            System.err.println("[ERROR] Поток вывода сервера был поврежден или закрыт: " + ex.getMessage());
        }
    }
}
