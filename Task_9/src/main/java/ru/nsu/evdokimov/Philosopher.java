package ru.nsu.evdokimov;

public class Philosopher extends Thread{

    private int hunger;
    private final int id;
    private final Fork leftFork;
    private final Fork rightFork;

    public Philosopher(int id, int hunger, Fork leftFork, Fork rightFork) {
        this.id = id;
        this.hunger = hunger;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
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
                getForks();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            hunger--;
        }
        System.out.println(this.getId() + " Phil go home");
    }

    private void thinking() throws InterruptedException {
        sleep(3000);
        System.out.println(this.getId() + " Phil finish thoughts");
    }

    private void getForks() throws InterruptedException {
        synchronized (leftFork) {
            synchronized (rightFork) {
                sleep(3000);
                System.out.println(this.getId() + " Got food");
            }
        }
    }

}
