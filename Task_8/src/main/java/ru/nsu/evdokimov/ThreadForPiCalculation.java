package ru.nsu.evdokimov;

import java.util.concurrent.BrokenBarrierException;

/**
 * Класс, инициализирующий запуск вычислений на конкретном потоке
 */
public class ThreadForPiCalculation implements Runnable {
    private final MultiThreadPiCalculator multiThreadPiCalculator;

    public ThreadForPiCalculation(MultiThreadPiCalculator multiThreadPiCalculator) {
        this.multiThreadPiCalculator = multiThreadPiCalculator;
    }

    @Override
    public void run() {
        try {
            multiThreadPiCalculator.performCalculations();
        } catch (BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
