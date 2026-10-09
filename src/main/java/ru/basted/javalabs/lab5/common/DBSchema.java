package ru.basted.javalabs.lab5.common;

public final class DBSchema {
    private DBSchema() {
        throw new UnsupportedOperationException("DBSchema — утилитный класс, создавать его экземпляры нельзя");
    }

    public static final String TABLE_NAME = "winning_table";
    public static final String TRIGGER_NAME = "checkDrawDate";

    public static final String DRAW_NUMBER = "draw_number";
    public static final String TICKET_NUMBER = "ticket_number";
    public static final String PRIZE_AMOUNT = "prize_amount";
    public static final String DRAW_DATE = "draw_date";
}
