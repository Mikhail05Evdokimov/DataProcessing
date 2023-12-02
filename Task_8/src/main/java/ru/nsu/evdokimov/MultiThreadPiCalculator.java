package ru.nsu.evdokimov;

import java.util.concurrent.BrokenBarrierException;

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
        int sign;
        if (identifier > 0) {
            sign = 1;
        }
        else {
            sign = -1;
        }
        if (threadsCount % 2 == 0) {
            for (int i = 0; Main.resource; i++) {
                amortizationCheck(i);
                calculationResult += 1.0/(identifier + i * threadsCount * 2 * sign);
            }
        }
        else {
            if (sign == -1) {
                for (int i = 0; Main.resource; i++) {
                    amortizationCheck(i);
                    calculationResult += 1.0/(identifier * sign * (-1) + i * threadsCount * 2 * sign);
                    sign *= -1;
                }
            }
            else {
                for (int i = 0; Main.resource; i++) {
                    amortizationCheck(i);
                    calculationResult += 1.0/(identifier * sign + i * threadsCount * 2 * sign);
                    sign *= -1;
                }
            }

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

