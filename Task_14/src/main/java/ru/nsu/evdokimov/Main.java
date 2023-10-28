package ru.nsu.evdokimov;

import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

/**
 * Task 14
 */
public class Main {

    public static final Semaphore semA = new Semaphore(0, true);
    public static final Semaphore semB = new Semaphore(0, true);
    public static final Semaphore semC = new Semaphore(0, true);

    public static void main(String[] args) {
        ChildThread partAThread = new ChildThread(semA, 1, 'A');
        ChildThread partBThread = new ChildThread(semB, 2, 'B');
        ChildThread partCThread = new ChildThread(semC, 3, 'C');
        partAThread.start();
        partBThread.start();
        partCThread.start();
        for (int i = 0; i < 10; i++) {
            try {
                semA.acquire();
                semB.acquire();
                semC.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("One more widget is ready");
        }
    }
}
