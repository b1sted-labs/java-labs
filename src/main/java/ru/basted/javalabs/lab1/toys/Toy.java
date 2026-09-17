package ru.basted.javalabs.lab1.toys;

import java.util.Objects;

public abstract class Toy implements Playable, Comparable<Toy> {
    private String name;
    private int price;
    private int targetAge;

    public Toy() {
        this("Без названия", 0, 0);
    }

    public Toy(String name, int price, int targetAge) {
        this.name = name;
        this.price = price;
        this.targetAge = targetAge;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTargetAge() {
        return targetAge;
    }

    public void setTargetAge(int targetAge) {
        this.targetAge = targetAge;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int compareTo(Toy other) {
        return Integer.compare(this.price, other.price);
    }

    @Override
    public String toString() {
        return String.format("Игрушка: %s, цена: %d, целевой возраст: %d", name, price, targetAge);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Toy toy)) return false;
        return price == toy.price && targetAge == toy.targetAge && Objects.equals(name, toy.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, targetAge);
    }
}
