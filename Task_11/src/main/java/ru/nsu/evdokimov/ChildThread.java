package ru.nsu.evdokimov;

public class ChildThread extends Thread{
    @Override
    public void run(){

        for (int i = 0; i < 10; i++) {
            try {
                Main.sem2.release();
                Main.sem1.acquire();
                System.out.println("Child thread hello!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Main.sem1.release();
            try {
                Main.sem2.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
