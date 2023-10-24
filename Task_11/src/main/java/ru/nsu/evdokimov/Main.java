package ru.nsu.evdokimov;

import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

/**
 * Task 10
 */
public class Main {

    public static final Semaphore sem1 = new Semaphore(1, true);
    public static final Semaphore sem2 = new Semaphore(1, true);

    public static void main(String[] args) throws InterruptedException {
        ChildThread firstThread = new ChildThread();
        firstThread.start();
        for (int i = 0; i < 10; i++) {
            try {
                sem2.release();
                sem1.acquire();
                System.out.println("Main thread hello!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sem1.release();
            sem2.acquire();
        }
    }
}
