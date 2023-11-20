package ru.nsu.evdokimov;

import java.util.concurrent.Semaphore;

public class ModuleThread extends Thread{
    private final Semaphore mainSemaphore;
    private final Semaphore semA;
    private final Semaphore semB;
    private final int workTime;

    public ModuleThread(Semaphore mainSemaphore, Semaphore semPartA, Semaphore semPartB, int workTime) {
        this.mainSemaphore = mainSemaphore;
        this.semA = semPartA;
        this.semB = semPartB;
        this.workTime = workTime;
    }

    @Override
    public void run(){

        for (int i = 0; i < 10; i++) {
            try {
                semA.acquire();
                semB.acquire();
                sleep(workTime);
                System.out.println("Module number [" + (i+1) + "] is ready");
                mainSemaphore.release();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}