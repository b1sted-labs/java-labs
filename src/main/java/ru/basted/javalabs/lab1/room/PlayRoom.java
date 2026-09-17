package ru.basted.javalabs.lab1.room;

import java.util.ArrayList;

import ru.basted.javalabs.lab1.toys.Toy;

public class PlayRoom {
    private final int initialBudget;
    private final int capacity;

    private int remainingBudget;
    private final ArrayList<Toy> toys = new ArrayList<>();

    public PlayRoom() {
        this(10000, 15);
    }

    public PlayRoom(int initialBudget, int capacity) {
        this.initialBudget = initialBudget;
        this.remainingBudget = initialBudget;
        this.capacity = capacity;
    }

    public void addToy(Toy toy) {
        int toyPrice = toy.getPrice();

        if (toyPrice > remainingBudget) {
            throw new IllegalStateException(
                    String.format("Не хватает средств! Остаток: %d, требуемая сумма: %d.", remainingBudget, toyPrice)
            );
        }

        if (toys.size() + 1 > capacity) {
            throw new IllegalStateException(String.format("Комната заполнена! Лимит игрушек: %d.", capacity));
        }

        toys.add(toy);
        remainingBudget -= toyPrice;
    }

    public int calculateTotalCost() {
        int totalCost = 0;

        for (Toy toy : toys) {
            totalCost += toy.getPrice();
        }

        return totalCost;
    }

    public void sortByPrice() {
        toys.sort(null);
    }

    public ArrayList<Toy> findToysByPriceRange(int min, int max) {
        ArrayList<Toy> toysInPriceRange = new ArrayList<>();

        for (Toy toy : toys) {
            if (toy.getPrice() >= min && toy.getPrice() <= max) {
                toysInPriceRange.add(toy);
            }
        }

        return toysInPriceRange;
    }

    public int getInitialBudget() {
        return initialBudget;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getRemainingBudget() {
        return remainingBudget;
    }

    public ArrayList<Toy> getToys() {
        return toys;
    }

    @Override
    public String toString() {
        StringBuilder toysInformation = new StringBuilder();

        for (Toy toy : toys) {
            toysInformation.append(toy.toString()).append("\n");
        }

        if (!toys.isEmpty()) {
            toysInformation.deleteCharAt(toysInformation.length() - 1);
        }

        return """
                ---
                Игровая комната:
                
                Выделено: %d руб. | Потрачено: %d руб. | Остаток: %d руб.
                Общее количество игрушек: %d
                
                Информация о игрушках:
                %s
                ---
                """.formatted(initialBudget, calculateTotalCost(), remainingBudget, toys.size(), toysInformation);
    }
}
