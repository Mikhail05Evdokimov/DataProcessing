package ru.nsu.evdokimov;

public class ThreadForPiCalculation implements Runnable {
    private final MultiThreadPiCalculator multiThreadPiCalculator;

    public ThreadForPiCalculation(MultiThreadPiCalculator multiThreadPiCalculator) {
        this.multiThreadPiCalculator = multiThreadPiCalculator;
    }

    @Override
    public void run() {
        multiThreadPiCalculator.performCalculations();
    }
}
