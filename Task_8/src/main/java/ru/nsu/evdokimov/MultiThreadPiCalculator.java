package ru.nsu.evdokimov;

import java.util.concurrent.BrokenBarrierException;

import static java.lang.Math.pow;
import static java.lang.Thread.sleep;

/**
 * Класс, отвечающий за вычисление числа пи на конкретном потоке
 * Экземпляры класса - объекты-вычислители
 */

public class MultiThreadPiCalculator {
    private final int identifier;
    private final int threadsCount;
    private double calculationResult = 0;
    private int currentIteration;
    private int maxIteration;

    public MultiThreadPiCalculator(final int identifier, int threadsCount) {
        this.threadsCount = threadsCount;
        this.identifier = identifier;
    }
    /**
     * Выполнение вычислений пока не придёт стоп сигнал.
     */
    public void performCalculations() throws BrokenBarrierException, InterruptedException {

        for (currentIteration = 0; Main.resource; currentIteration++) {
            calculationResult += pow(-1, identifier + (currentIteration * threadsCount)) / (2 * (identifier + currentIteration * threadsCount) + 1);
        }

        //Tells Main that this thread finished
        try {
            Main.firstAmortizationBarrier.await();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        //Main tells this thread the maximum iteration
        try {
            Main.secondAmortizationBarrier.await();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        //Amortization in case of difference in iterations in threads
        for (currentIteration = currentIteration + 1; currentIteration < maxIteration; currentIteration++) {
            calculationResult += pow(-1, identifier + (currentIteration * threadsCount)) / (2 * (identifier + currentIteration * threadsCount) + 1);
        }

        try {
            Main.barrier.await();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * ВАЖНО!
     * Самый правильный способ вычисления и получения данных,
     * и для демонстрации работы барьера отлично подходит.
     *
     * @return Результат вычислений.
     */
    public double getCalculationResult() {
        return calculationResult;
    }

    public int getCurrentIteration() {
        return currentIteration;
    }

    public void setMaxIteration(int max) {
        maxIteration = max;
    }
}

