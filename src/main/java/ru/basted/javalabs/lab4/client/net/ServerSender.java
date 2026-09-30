package ru.basted.javalabs.lab4.client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ru.basted.javalabs.lab4.common.Protocol;

public class ServerSender implements Runnable {
    private final Socket socket;

    public ServerSender(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in, Protocol.CHARSET));
             PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true, Protocol.CHARSET)) {
            while (true) {
                String command = consoleReader.readLine();
                if (command == null || command.equals(Protocol.EXIT_COMMAND)) {
                    System.exit(0);
                }

                socketWriter.println(command);
            }
        } catch (IOException ex) {
            System.err.println("[ERROR] Потоки ввода были повреждены или закрыты: " + ex.getMessage());
        }
    }
}
