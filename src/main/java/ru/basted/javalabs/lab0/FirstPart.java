package ru.basted.javalabs.lab0;

import java.util.Scanner;

public class FirstPart {
    private static final int FIRST_ARRAY_SIZE = 5;
    private static final int SECOND_ARRAY_SIZE = 7;
    private static final int THIRD_ARRAY_SIZE = 3;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("=== Ввод массивов ===");
        int[] firstArray = arrayInput(scan, FIRST_ARRAY_SIZE);
        int[] secondArray = arrayInput(scan, SECOND_ARRAY_SIZE);
        int[] thirdArray = arrayInput(scan, THIRD_ARRAY_SIZE);

        int[] formedFirstArray = buildNewArray(firstArray);
        int[] formedSecondArray = buildNewArray(secondArray);
        int[] formedThirdArray = buildNewArray(thirdArray);

        int firstArrayElementsInRange = countArrayElementsInRange(formedFirstArray);
        int secondArrayElementsInRange = countArrayElementsInRange(formedSecondArray);
        int thirdArrayElementsInRange = countArrayElementsInRange(formedThirdArray);

        System.out.println("\n=== Вывод массивов ===");
        resultOutput(firstArray, formedFirstArray, firstArrayElementsInRange);
        resultOutput(secondArray, formedSecondArray, secondArrayElementsInRange);
        resultOutput(thirdArray, formedThirdArray, thirdArrayElementsInRange);

        scan.close();
    }

    private static int[] arrayInput(Scanner scan, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Размер массива должен быть строго больше нуля");
        }

        System.out.print("Введите " + size + " элементов: ");
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = scan.nextInt();
        }

        return array;
    }

    private static void arrayOutput(int[] array) {
        for (int j : array) {
            System.out.print(j + " ");
        }

        System.out.println();
    }

    private static void resultOutput(int[] initialArray, int[] formedArray, int arrayCount) {
        System.out.print("Исходный массив: ");
        arrayOutput(initialArray);
        System.out.print("Сформированный массив: ");
        arrayOutput(formedArray);
        System.out.printf("Количество элементов массива, принадлежащих отрезку [1, 4]: %d\n\n", arrayCount);
    }

    private static int countArrayElementsInRange(int[] array) {
        int count = 0;

        for (int j : array) {
            if (j >= 1 && j <= 4) {
                count++;
            }
        }

        return count;
    }

    private static int[] buildNewArray(int[] initialArray) {
        int[] newArray = initialArray.clone();

        for (int i = 0; i < newArray.length; i++) {
            if (newArray[i] % 2 == 0) {
                newArray[i] = 0;
            }
        }

        int temp = newArray[0];
        newArray[0] = newArray[newArray.length - 1];
        newArray[newArray.length - 1] = temp;

        return newArray;
    }
}
