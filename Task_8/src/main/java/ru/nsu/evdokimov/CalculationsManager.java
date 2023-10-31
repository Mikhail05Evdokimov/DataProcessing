package ru.nsu.evdokimov;

import java.util.ArrayList;
import java.util.List;
public final class CalculationsManager {
    private final List<MultiThreadPiCalculator> multiThreadPiCalculators;
    public CalculationsManager(final int threadsCount) {
        this.multiThreadPiCalculators = new ArrayList<>(threadsCount);
        int startValueForCurrentThread = 3;
        int sign = -1;
        for (int i = 0; i < threadsCount; i++) {
            multiThreadPiCalculators.add(i, new MultiThreadPiCalculator(startValueForCurrentThread*sign, threadsCount));
            startValueForCurrentThread += 2;
            sign *= -1;
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
        System.out.println("Pi number: " + (1 + result) * 4);
    }
    /**
     * @return Количество доступных потоков для выполнения
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