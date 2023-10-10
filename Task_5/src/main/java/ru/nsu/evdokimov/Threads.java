package ru.nsu.evdokimov;

import static java.lang.Thread.sleep;

/**
 * Task 4
 */
public class Threads {

    public static void main(String[] args) throws InterruptedException {

        ThreadPrinter firstThread = new ThreadPrinter();
        firstThread.start();
        sleep(2000);
        firstThread.interrupt();
    }
}
