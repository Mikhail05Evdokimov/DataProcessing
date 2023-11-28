package ru.nsu.evdokimov;

import java.util.ArrayList;
import java.util.List;
public final class CalculationsManager {
    private final List<MultiThreadPiCalculator> multiThreadPiCalculators;
    public CalculationsManager(final int departmentsCount) {
        this.multiThreadPiCalculators = new ArrayList<>(departmentsCount);
        for (int i = 0; i < departmentsCount; i++) {
            multiThreadPiCalculators.add(i, new MultiThreadPiCalculator(i+1, departmentsCount));
        }
    }

    /**
     * Вывод результата.
     * P.S. Актуально после того, как все потоки выполнят свою работу.
     */
    public void showCollaborativeResult() {
        System.out.println("All threads have completed their work.");
        final double result = multiThreadPiCalculators.stream()
            .map(MultiThreadPiCalculator::getCalculationResult)
            .reduce(Double::sum)
            .orElse(-1.0);
        System.out.println("Pi number is: " + (1 + result) * 4);
    }
    /**
     * @return Количество доступных потоков для симуляции выполнения
    работы.
     */
    public int getThreadsCount() {
        return multiThreadPiCalculators.size();
    }
    /**
     * @param index Index для текущего свободного потока.
     * @return Свободный поток для выполнения работы.
     */
    public MultiThreadPiCalculator getFreeThread(final int index) {
        return multiThreadPiCalculators.get(index);
    }
}
