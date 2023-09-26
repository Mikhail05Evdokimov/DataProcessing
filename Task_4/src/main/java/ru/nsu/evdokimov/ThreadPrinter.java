package ru.nsu.evdokimov;

public class ThreadPrinter extends Thread{

    @Override
    public void run(){
        while (!(this.isInterrupted())) {
            System.out.println("Hello world!");
        }
    }
}
