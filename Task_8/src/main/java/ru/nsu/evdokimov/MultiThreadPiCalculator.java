package ru.nsu.evdokimov;

import java.util.concurrent.BrokenBarrierException;

import static java.lang.Math.pow;
import static java.lang.Thread.sleep;

/**
 * Класс, отвечающий за вычисление числа пи на конкретном потоке
 * Экземпляры класса - объекты-вычислятели
 */

public class MultiThreadPiCalculator {
    private final int identifier;
    private final int threadsCount;
    private double calculationResult = 0;

    public MultiThreadPiCalculator(final int identifier, int threadsCount) {
        this.threadsCount = threadsCount;
        this.identifier = identifier;
    }
    /**
     * Выполнение вычислений пока не придёт стоп сигнал.
     */
    public void performCalculations() throws BrokenBarrierException, InterruptedException {

        for (int i = 0; Main.resource; i++) {
            amortizationCheck(i);
            calculationResult += pow(-1, identifier + (i * threadsCount)) / (2 * (identifier + i * threadsCount) + 1);
        }

        try {
            sleep(10);
            Main.amortizationBarrier.await();
            Main.barrier.await();

        } catch ( InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    private void amortizationCheck(int i) throws BrokenBarrierException, InterruptedException {
        if ((i + 1) % 10 == 0) {
            sleep(10);
            Main.amortizationBarrier.await();
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
}

