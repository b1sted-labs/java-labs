package ru.basted.javalabs.lab1.toys;

import java.util.Objects;

public class Doll extends Toy {
    private boolean hasSound;

    public Doll() {
        this("Кукла", 0, 0, false);
    }

    public Doll(String name, int price, int targetAge, boolean hasSound) {
        super(name, price, targetAge);
        this.hasSound = hasSound;
    }

    public boolean isHasSound() {
        return hasSound;
    }

    public void setHasSound(boolean hasSound) {
        this.hasSound = hasSound;
    }

    @Override
    public void play() {
        System.out.println("Играем с куклой.");
    }

    @Override
    public String toString() {
        return String.format("%s, %s", super.toString(), hasSound ? "со звуком" : "без звука");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Doll doll)) return false;
        if (!super.equals(o)) return false;
        return hasSound == doll.hasSound;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hasSound);
    }
}
