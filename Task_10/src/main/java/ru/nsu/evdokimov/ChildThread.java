package ru.nsu.evdokimov;

public class ChildThread extends Thread{
    @Override
    public void run(){

        for (int i = 0; i < 10; i++) {
            synchronized (Main.printer) {
                while (Main.printer.getTurn()) {
                    try {
                        Main.printer.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                    System.out.println("Child thread hello!");
                    Main.printer.setTurn(true);
                    Main.printer.notifyAll();
            }
        }
    }
}
