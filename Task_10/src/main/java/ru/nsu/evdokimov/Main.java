package ru.nsu.evdokimov;

/**
 * Task 10
 */
public class Main {

    public static final SynchronousPrinter printer = new SynchronousPrinter(true);

    public static void main(String[] args) throws InterruptedException {
        ChildThread firstThread = new ChildThread();
        firstThread.start();
        for (int i = 0; i < 10; i++) {
            synchronized (printer) {
                while (!(printer.getTurn())) {
                    printer.wait();
                }
                    System.out.println("Main thread hello!");
                    printer.setTurn(false);
                    printer.notifyAll();
            }
        }
    }
}
