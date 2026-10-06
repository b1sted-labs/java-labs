package ru.basted.javalabs.lab4.common;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class Protocol {
    public static final int DEFAULT_PORT = 8080;
    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static final String END_OF_RESPONSE = ".end_of_response!";
    public static final String EXIT_COMMAND = "exit";

    private Protocol() {
        throw new UnsupportedOperationException("Protocol — утилитный класс, создавать его экземпляры нельзя");
    }
}
