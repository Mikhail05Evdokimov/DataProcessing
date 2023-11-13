package ru.nsu.evdokimov;

import java.util.concurrent.Semaphore;

public class Philosopher extends Thread{

    private int hunger;
    private final int id;
    private final Fork leftFork;
    private final Fork rightFork;
    private final int eatTime;
    private final int thinkTime;

    public Philosopher(int id, int hunger, Fork leftFork, Fork rightFork) {
        this.id = id;
        this.hunger = hunger;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        thinkTime = 300;
        eatTime = 300;
    }

    public long getId() {
        return id;
    }

    @Override
    public void run() {
        while (hunger > 0) {
            try {
                thinking();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                while (true) {
                    if (getForks()) break;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(this.getId() + " Phil went home");
    }

    private void thinking() throws InterruptedException {
        sleep(thinkTime);
        System.out.println(this.getId() + " Phil finish thoughts");
    }

    private boolean getForks() throws InterruptedException {
        if (leftFork.getFork()) {
            if (rightFork.getFork()) {
                sleep(eatTime);
                System.out.println(this.getId() + " Got food");
                hunger--;
                rightFork.unlock();
                leftFork.unlock();
                return true;
            }
            else {
                leftFork.unlock();
            }
        }
        Semaphore sem = new Semaphore(1, true);
        return false;
    }
}
