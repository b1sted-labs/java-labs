package ru.basted.javalabs.lab1;

import java.util.ArrayList;

import ru.basted.javalabs.lab1.room.PlayRoom;
import ru.basted.javalabs.lab1.toys.Ball;
import ru.basted.javalabs.lab1.toys.Block;
import ru.basted.javalabs.lab1.toys.Car;
import ru.basted.javalabs.lab1.toys.Doll;
import ru.basted.javalabs.lab1.toys.Toy;

public class Main {
    private static final int MIN_PRICE = 800;
    private static final int MAX_PRICE = 1600;

    public static void main(String[] args) {
        PlayRoom playRoom = new PlayRoom();

        try {
            playRoom.addToy(new Car("Hot Wheels", 350, 3, Car.Size.SMALL));
            playRoom.addToy(new Car("Полицейский седан", 850, 4, Car.Size.MEDIUM));
            playRoom.addToy(new Car("Самосвал Карпат", 2100, 3, Car.Size.BIG));

            playRoom.addToy(new Doll("Пупс мягконабивной", 1200, 1, true));
            playRoom.addToy(new Doll("Кукла Модница", 1800, 6, false));

            playRoom.addToy(new Ball("Мячик резиновый с рисунком", 400, 1, 15));
            playRoom.addToy(new Ball("Футбольный мяч №5", 1300, 7, 22));

            playRoom.addToy(new Block("Деревянный городок", 1500, 2, 50));
        } catch (IllegalStateException ex) {
            System.err.println("Ошибка при наполнении игровой комнаты: " + ex.getMessage());
        }

        System.out.println(playRoom);

        System.out.println("Исходный массив игрушек: ");
        arrayOutput(playRoom.getToys());

        System.out.println("\nМассив игрушек после сортировки по цене: ");
        playRoom.sortByPrice();
        arrayOutput(playRoom.getToys());

        System.out.printf("\nНайденные игрушки в ценовом диапазоне [%d, %d]:%n", MIN_PRICE, MAX_PRICE);
        ArrayList<Toy> toysInPriceRange = playRoom.findToysByPriceRange(MIN_PRICE, MAX_PRICE);
        arrayOutput(toysInPriceRange);
    }

    private static void arrayOutput(ArrayList<Toy> toys) {
        for (Toy toy : toys) {
            System.out.println(toy);
        }
    }
}
