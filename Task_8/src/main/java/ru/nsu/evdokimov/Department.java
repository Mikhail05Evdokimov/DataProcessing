package ru.nsu.evdokimov;

import java.util.concurrent.BrokenBarrierException;

public class Department {
    private final int identifier;
    private final int threadsCount;
    private double calculationResult = 0;

    public Department(final int identifier, int threadsCount) {
        this.threadsCount = threadsCount;
        this.identifier = identifier;
    }
    /**
     * Симуляция работы длительностью в workingSeconds секунд.
     * В данном случае просто вычисляем сумму.
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
            for (int i = 0; Main.resource; i++) {
                calculationResult += 1.0/(identifier + i * threadsCount * 2 * sign);
            }
        }
        else {
            for (int i = 0; Main.resource; i++) {
                calculationResult += 1.0/(identifier + i * threadsCount * 2 * sign);
                sign *= -1;
            }
        }
        try {

                System.out.println(this.getIdentifier() + " Ready");
                Main.berrier.await();

        } catch ( InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * @return Уникальный идентификатор для отдела,
     * по которому мы можем отличить его от других.
     */
    public int getIdentifier() {
        return identifier;
    }
    /**
     * ВАЖНО!
     * Далеко не самый правильный способ вычисления и получения данных,
     * но для демонстрации работы барьера пойдёт.
     *
     * @return Результат вычислений.
     */
    public double getCalculationResult() {
        return calculationResult;
    }
}

