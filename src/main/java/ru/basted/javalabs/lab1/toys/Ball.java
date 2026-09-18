package ru.basted.javalabs.lab1.toys;

import java.util.Objects;

public class Ball extends Toy {
    private int diameter;

    public Ball() {
        this("Мяч", 0, 0, 0);
    }

    public Ball(String name, int price, int targetAge, int diameter) {
        super(name, price, targetAge);
        this.diameter = diameter;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    @Override
    public void play() {
        System.out.println("Катаем мяч.");
    }

    @Override
    public String toString() {
        return String.format("%s, диаметр: %d", super.toString(), diameter);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ball ball)) return false;
        if (!super.equals(o)) return false;
        return diameter == ball.diameter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), diameter);
    }
}
