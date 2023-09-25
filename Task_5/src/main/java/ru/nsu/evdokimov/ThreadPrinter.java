package ru.nsu.evdokimov;

public class ThreadPrinter extends Thread{

    private boolean interruptFlag = true;

    @Override
    public void run(){
        while (interruptFlag) {
            System.out.println("Hello world!");
        }
        System.out.println("T1: Interrupted by Main");
    }

    public void setInterruptFlag() {
        synchronized ((Object) interruptFlag) {
            interruptFlag = false;
        }
    }
}
