package ru.nsu.evdokimov;

/**
 * Task 2
 */
public class Threads {

    public static void main(String[] args) throws InterruptedException {
        ThreadPrinter firstThread = new ThreadPrinter();
        firstThread.start();
        firstThread.join();
        for (int i = 0; i < 10; i++) {
            System.out.println("World!");
        }
    }
}
