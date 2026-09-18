package ru.basted.javalabs.lab1.toys;

import java.util.Objects;

public class Block extends Toy {
    private int numberOfPieces;

    public Block() {
        this("Кубики", 0, 0, 0);
    }

    public Block(String name, int price, int targetAge, int numberOfPieces) {
        super(name, price, targetAge);
        this.numberOfPieces = numberOfPieces;
    }

    public int getNumberOfPieces() {
        return numberOfPieces;
    }

    public void setNumberOfPieces(int numberOfPieces) {
        this.numberOfPieces = numberOfPieces;
    }

    @Override
    public void play() {
        System.out.println("Складываем пирамиду из кубиков.");
    }

    @Override
    public String toString() {
        return String.format("%s, количество кубиков: %d", super.toString(), numberOfPieces);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Block block)) return false;
        if (!super.equals(o)) return false;
        return numberOfPieces == block.numberOfPieces;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), numberOfPieces);
    }
}
