package ru.nsu.evdokimov;

import java.util.ArrayList;
import java.util.List;

/**
 * Task 3
 */
public class Threads {

    public static void main(String[] args) {

        List<String> firstList = new ArrayList<>();

        firstList.add("T1: Hello");
        firstList.add("T1: World");
        firstList.add("T1: !");
        ThreadPrinter firstThread = new ThreadPrinter(firstList);
        firstList.clear();

        firstList.add("T2: Hello");
        firstList.add("T2: World");
        firstList.add("T2: !");
        ThreadPrinter secondThread = new ThreadPrinter(firstList);
        firstList.clear();

        firstList.add("T3: Hello");
        firstList.add("T3: World");
        firstList.add("T3: !");
        ThreadPrinter thirdThread = new ThreadPrinter(firstList);
        firstList.clear();

        firstList.add("T4: Hello");
        firstList.add("T4: World");
        firstList.add("T4: !");
        ThreadPrinter fourthThread = new ThreadPrinter(firstList);

        firstThread.start();
        secondThread.start();
        thirdThread.start();
        fourthThread.start();
    }
}
