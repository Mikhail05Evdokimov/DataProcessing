package ru.nsu.evdokimov;

import java.util.concurrent.Semaphore;

/**
 * Task 14
 */
public class Main {

    public static final Semaphore semA = new Semaphore(0, true);
    public static final Semaphore semB = new Semaphore(0, true);
    public static final Semaphore semC = new Semaphore(0, true);
    public static final Semaphore semModule = new Semaphore(0, true);

    public static void main(String[] args) {
        PartsThread partAThread = new PartsThread(semA, 1, 'A');
        PartsThread partBThread = new PartsThread(semB, 2, 'B');
        PartsThread partCThread = new PartsThread(semC, 3, 'C');
        ModuleThread moduleThread = new ModuleThread(semModule, semA, semB, 1);
        partAThread.start();
        partBThread.start();
        partCThread.start();
        moduleThread.start();
        for (int i = 0; i < 10; i++) {
            try {
                semModule.acquire();
                semC.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("One more widget is ready. Total: " + (i+1));
        }
    }
}
