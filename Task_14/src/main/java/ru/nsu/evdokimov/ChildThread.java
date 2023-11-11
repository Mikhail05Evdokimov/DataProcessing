package ru.nsu.evdokimov;

import java.util.concurrent.Semaphore;

public class ChildThread extends Thread{

    private final Semaphore semaphore;
    private final int workTime;
    private final char name;

    public ChildThread(Semaphore semaphore, int workTime, char name) {
        this.semaphore = semaphore;
        this.workTime = workTime * 1000;
        this.name = name;
    }

    @Override
    public void run(){

        for (int i = 0; i < 10; i++) {
            try {
                sleep(workTime);
                semaphore.release();
                System.out.println("Part " + name + " is ready");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
