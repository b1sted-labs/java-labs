package ru.basted.javalabs.lab1.toys;

import java.util.Objects;

public class Car extends Toy {
    public enum Size {
        SMALL,
        MEDIUM,
        BIG,
        UNKNOWN,
    };

    private Size size;

    public Car() {
        this("Машинка", 0, 0, Size.UNKNOWN);
    }

    public Car(String name, int price, int targetAge, Size size) {
        super(name, price, targetAge);
        this.size = size;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    public void play() {
        System.out.println("Катаем машинку по полу.");
    }

    @Override
    public String toString() {
        return String.format("%s, размер: %s", super.toString(), size);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Car car)) return false;
        if (!super.equals(o)) return false;
        return size == car.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), size);
    }
}
