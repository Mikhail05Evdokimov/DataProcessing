package ru.nsu.evdokimov;

import java.util.*;

/**
 * Task 12
 */
public class Main {

    public static final List<String> stringList = new LinkedList<>();

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ChildThread childThread = new ChildThread();
        childThread.start();
        String string;
        do {
            string= scan.nextLine();
            if (string.isEmpty()) {
                synchronized (stringList) {
                    System.out.println(stringList);
                }
            }
            else {
                synchronized (stringList) {
                    while (string.length() > 10) {
                        stringList.add(string.substring(0, 9));
                        string = string.substring(10);
                    }
                    stringList.add(string);
                }
            }
        } while (!(Objects.equals(string, "stop")));
        childThread.interrupt();
    }
}
