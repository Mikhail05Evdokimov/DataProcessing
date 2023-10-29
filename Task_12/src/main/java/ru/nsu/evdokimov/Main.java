package ru.nsu.evdokimov;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Task 12
 */
public class Main {

    public static final List<String> stringList = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ChildThread childThread = new ChildThread();
        childThread.start();
        for (;;) {
            String string = scan.nextLine();
            if (Objects.equals(string, "stop")) {
                childThread.interrupt();
                break;
            }
            if (string.isEmpty()) {
                synchronized (stringList) {
                    System.out.println(stringList);
                }
            }
            else {
                synchronized (stringList) {
                    stringList.add(string);
                }
            }
        }
    }
}
