package ru.basted.javalabs.lab0;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

record ListResult(ArrayList<Integer> arrayList, LinkedList<Integer> linkedList) {
}

class ElementsCount {
    // [1; 4]
    int arrayListElementsInRange;
    int linkedListElementsInRange;

    public ElementsCount() {
        this.arrayListElementsInRange = 0;
        this.linkedListElementsInRange = 0;
    }
}

public class SecondPart {
    private static final int FIRST_ARRAY_SIZE = 5;
    private static final int SECOND_ARRAY_SIZE = 7;
    private static final int THIRD_ARRAY_SIZE = 3;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("=== Ввод динамических массивов ===");
        ListResult firstLists = listsInput(scan, FIRST_ARRAY_SIZE);
        ListResult secondLists = listsInput(scan, SECOND_ARRAY_SIZE);
        ListResult thirdLists = listsInput(scan, THIRD_ARRAY_SIZE);

        ListResult builtFirstLists = buildNewLists(firstLists);
        ListResult builtSecondLists = buildNewLists(secondLists);
        ListResult builtThirdLists = buildNewLists(thirdLists);

        ElementsCount elementsInFirstBuiltLists = countElementsInRange(builtFirstLists);
        ElementsCount elementsInSecondBuiltLists = countElementsInRange(builtSecondLists);
        ElementsCount elementsInThirdBuiltLists = countElementsInRange(builtThirdLists);

        System.out.println("\n=== Вывод массивов/списков ===");
        System.out.println("Первые массивы: ");
        resultsOutput(firstLists, builtFirstLists, elementsInFirstBuiltLists);
        resultsOutput(secondLists, builtSecondLists, elementsInSecondBuiltLists);
        resultsOutput(thirdLists, builtThirdLists, elementsInThirdBuiltLists);

        scan.close();
    }

    private static ListResult listsInput(Scanner scan, int size) {
        System.out.print("Введите " + size + " элементов: ");

        ArrayList<Integer> arrayList = new ArrayList<>(size);
        LinkedList<Integer> linkedList = new LinkedList<>();

        for (int i = 0; i < size; i++) {
            int currentNumber = scan.nextInt();
            arrayList.add(currentNumber);
            linkedList.add(currentNumber);
        }

        return new ListResult(arrayList, linkedList);
    }

    private static ListResult buildNewLists(ListResult initialLists) {
        ArrayList<Integer> newArrayList = new ArrayList<>(initialLists.arrayList());
        LinkedList<Integer> newLinkedList = new LinkedList<>(initialLists.linkedList());

        for (int i : newArrayList) {
            if (i % 2 == 0) {
                newArrayList.set(newArrayList.indexOf(i), 0);
                newLinkedList.set(newLinkedList.indexOf(i), 0);
            }
        }

        int arraysSize = newArrayList.size();
        int lastIndex = arraysSize - 1;

        int temp = newArrayList.get(lastIndex);

        newArrayList.set(lastIndex, newArrayList.getFirst());
        newLinkedList.set(lastIndex, newArrayList.getFirst());

        newArrayList.set(0, temp);
        newLinkedList.set(0, temp);

        return new ListResult(newArrayList, newLinkedList);
    }

    private static ElementsCount countElementsInRange(ListResult lists) {
        ElementsCount elementsCount = new ElementsCount();

        for (int i : lists.arrayList()) {
            if (i >= 1 && i <= 4) {
                elementsCount.arrayListElementsInRange++;
                elementsCount.linkedListElementsInRange++;
            }
        }

        return elementsCount;
    }

    private static void arrayOutput(List<Integer> array) {
        for (int i : array) {
            System.out.print(i + " ");
        }

        System.out.println();
    }

    private static void resultsOutput(ListResult listResult, ListResult builtLists, ElementsCount elementsCount) {
        System.out.println("Исходные массивы: ");
        System.out.print("ArrayList: ");
        arrayOutput(listResult.arrayList());
        System.out.print("LinkedList: ");
        arrayOutput(listResult.linkedList());

        System.out.println("Сформированные массивы: ");
        System.out.print("ArrayList: ");
        arrayOutput(builtLists.arrayList());
        System.out.print("LinkedList: ");
        arrayOutput(builtLists.linkedList());

        System.out.printf("Количество элементов массивов, принадлежащих отрезку [1, 4]: %d|%d\n\n",
                elementsCount.arrayListElementsInRange, elementsCount.linkedListElementsInRange);
    }
}
