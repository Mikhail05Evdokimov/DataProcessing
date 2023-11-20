package ru.nsu.evdokimov;

import java.util.concurrent.Semaphore;

public class PartsThread extends Thread{

    private final Semaphore semaphore;
    private final int workTime;
    private final char name;

    public PartsThread(Semaphore semaphore, int workTime, char name) {
        this.semaphore = semaphore;
        this.workTime = workTime * 1000;
        this.name = name;
    }

    @Override
    public void run(){

        for (int i = 0; i < 10; i++) {
            try {
                sleep(workTime);
                System.out.println("Part " + name + " [" + (i+1) + "] is ready");
                semaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
