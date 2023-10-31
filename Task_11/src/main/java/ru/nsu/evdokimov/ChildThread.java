package ru.nsu.evdokimov;

public class ChildThread extends Thread{
    @Override
    public void run(){

        for (int i = 0; i < 10; i++) {
            try {
                Main.sem2.acquire();
                System.out.println("Child thread hello!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Main.sem1.release();
        }

    }
}
