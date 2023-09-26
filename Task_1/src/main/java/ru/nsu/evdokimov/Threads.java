package ru.nsu.evdokimov;

/**
 * Task 1
 */
public class Threads {

    public static void main(String[] args) {
        ThreadPrinter firstThread = new ThreadPrinter();
        firstThread.start();
        for (int i = 0; i < 10; i++) {
            System.out.println("World!");
        }
    }
}
