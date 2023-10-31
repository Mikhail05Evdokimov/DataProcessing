package ru.nsu.evdokimov;

import java.util.concurrent.BrokenBarrierException;

public class MultiThreadPiCalculator {
    private final int identifier;
    private final int iterations;
    private final int threadsCount;
    private double calculationResult = 0;

    public MultiThreadPiCalculator(final int identifier, int threadsCount) {
        this.threadsCount = threadsCount;
        this.identifier = identifier;
        this.iterations = 200;
    }
    /**
     * Вычисление на iterations итераций
     */
    public void performCalculations() {
        int sign;
        if (identifier > 0) {
            sign = 1;
        }
        else {
            sign = -1;
        }
        if (threadsCount % 2 == 0) {
            for (int i = 0; i < iterations; i++) {
                calculationResult += 1.0/(identifier + i * threadsCount * 2 * sign);
            }
        }
        else {
            if (sign == -1) {
                for (int i = 0; i < iterations; i++) {
                    calculationResult += 1.0/(identifier * sign * (-1) + i * threadsCount * 2 * sign);
                    sign *= -1;
                }
            }
            else {
                for (int i = 0; i < iterations; i++) {
                    calculationResult += 1.0/(identifier * sign + i * threadsCount * 2 * sign);
                    sign *= -1;
                }
            }

        }
        try {

                System.out.println(this.getIdentifier() + " Ready");
                Main.barrier.await();

        } catch ( InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * @return Уникальный идентификатор для потока,
     * по которому мы можем отличить его от других.
     */
    public int getIdentifier() {
        return identifier;
    }
    /**
     * ВАЖНО!
     * Самый правильный способ вычисления и получения данных,
     * и для демонстрации работы барьера подходит отлично.
     *
     * @return Результат вычислений.
     */
    public double getCalculationResult() {
        return calculationResult;
    }
}

