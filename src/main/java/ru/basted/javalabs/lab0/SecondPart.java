package ru.basted.javalabs.lab0;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SecondPart {
    private static final int FIRST_ARRAY_SIZE = 5;
    private static final int SECOND_ARRAY_SIZE = 7;
    private static final int THIRD_ARRAY_SIZE = 3;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("=== Ввод динамических массивов ===");
        ArrayList<Integer> firstArrayList = arrayListInput(scan, FIRST_ARRAY_SIZE);
        ArrayList<Integer> secondArrayList = arrayListInput(scan, SECOND_ARRAY_SIZE);
        ArrayList<Integer> thirdArrayList = arrayListInput(scan, THIRD_ARRAY_SIZE);

        System.out.println("=== Ввод связных списков ===");
        LinkedList<Integer> firstLinkedList = linkedListInput(scan, FIRST_ARRAY_SIZE);
        LinkedList<Integer> secondLinkedList = linkedListInput(scan, SECOND_ARRAY_SIZE);
        LinkedList<Integer> thirdLinkedList = linkedListInput(scan, THIRD_ARRAY_SIZE);

        ArrayList<Integer> formedFirstArrayList = buildNewArrayList(firstArrayList);
        ArrayList<Integer> formedSecondArrayList = buildNewArrayList(secondArrayList);
        ArrayList<Integer> formedThirdArrayList = buildNewArrayList(thirdArrayList);

        LinkedList<Integer> formedFirstLinkedList = buildNewLinkedList(firstLinkedList);
        LinkedList<Integer> formedSecondLinkedList = buildNewLinkedList(secondLinkedList);
        LinkedList<Integer> formedThirdLinkedList = buildNewLinkedList(thirdLinkedList);

        int firstArrayListElementsInRange = countArrayElementsInRange(formedFirstArrayList);
        int secondArrayListElementsInRange = countArrayElementsInRange(formedSecondArrayList);
        int thirdArrayListElementsInRange = countArrayElementsInRange(formedThirdArrayList);

        int firstLinkedListElementsInRange = countArrayElementsInRange(formedFirstLinkedList);
        int secondLinkedListElementsInRange = countArrayElementsInRange(formedSecondLinkedList);
        int thirdLinkedListElementsInRange = countArrayElementsInRange(formedThirdLinkedList);

        System.out.println("\n=== Вывод массивов/списков ===");
        System.out.println("Первый массив: ");
        resultsOutput(firstArrayList, formedFirstArrayList, firstArrayListElementsInRange);
        resultsOutput(firstLinkedList, formedFirstLinkedList, firstLinkedListElementsInRange);

        System.out.println("Второй массив: ");
        resultsOutput(secondArrayList, formedSecondArrayList, secondArrayListElementsInRange);
        resultsOutput(secondLinkedList, formedSecondLinkedList, secondLinkedListElementsInRange);

        System.out.println("Третий массив: ");
        resultsOutput(thirdArrayList, formedThirdArrayList, thirdArrayListElementsInRange);
        resultsOutput(thirdLinkedList, formedThirdLinkedList, thirdLinkedListElementsInRange);

        scan.close();
    }

    private static ArrayList<Integer> arrayListInput(Scanner scan, int size) {
        System.out.println("Введите " + size + " элементов: ");
        ArrayList<Integer> array = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            array.add(scan.nextInt());
        }

        return array;
    }

    private static LinkedList<Integer> linkedListInput(Scanner scan, int size) {
        System.out.println("Введите " + size + " элементов: ");
        LinkedList<Integer> array = new LinkedList<>();

        for (int i = 0; i < size; i++) {
            array.add(scan.nextInt());
        }

        return array;
    }

    private static void arrayOutput(List<Integer> array) {
        for (int i : array) {
            System.out.print(i + " ");
        }

        System.out.println();
    }

    private static void resultsOutput(List<Integer> initialList, List<Integer> formedList, int elementsInRange) {
        System.out.print("Исходный массив: ");
        arrayOutput(initialList);
        System.out.print("Сформированный массив: ");
        arrayOutput(formedList);
        System.out.printf("Количество элементов массива, принадлежащих отрезку [1, 4]: %d\n\n", elementsInRange);
    }

    private static ArrayList<Integer> buildNewArrayList(ArrayList<Integer> initialArray) {
        int initialArraySize = initialArray.size();
        ArrayList<Integer> newArray = (ArrayList<Integer>) initialArray.clone();

        for (int i : newArray) {
            if (i % 2 == 0) {
                newArray.set(newArray.indexOf(i), 0);
            }
        }

        int lastIndex = initialArraySize - 1;
        int temp = newArray.get(initialArraySize - 1);
        newArray.set(lastIndex, newArray.getFirst());
        newArray.set(0, temp);

        return newArray;
    }

    private static LinkedList<Integer> buildNewLinkedList(LinkedList<Integer> initialArray) {
        int initialArraySize = initialArray.size();
        LinkedList<Integer> newArray = (LinkedList<Integer>) initialArray.clone();

        for (int i : newArray) {
            if (i % 2 == 0) {
                newArray.set(newArray.indexOf(i), 0);
            }
        }

        int lastIndex = initialArraySize - 1;
        int temp = newArray.get(initialArraySize - 1);
        newArray.set(lastIndex, newArray.getFirst());
        newArray.set(0, temp);

        return newArray;
    }

    private static int countArrayElementsInRange(List<Integer> array) {
        int count = 0;

        for (int i : array) {
            if (i >= 1 && i <= 4) {
                count++;
            }
        }

        return count;
    }
}
