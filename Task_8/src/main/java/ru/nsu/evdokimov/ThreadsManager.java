package ru.nsu.evdokimov;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, отвечающий за создание потоков,
 * распределение объектов-вычислятелей между потоками
 * и запуск потоков.
 */
public final class ThreadsManager {

    private final List<ThreadForPiCalculation> threadForPiCalculations;
    public ThreadsManager(final CalculationsManager calculationsManager) {
        this.threadForPiCalculations = new ArrayList<>();
        for (int i = 0; i < calculationsManager.getThreadsCount(); i++) {
            threadForPiCalculations.add(new ThreadForPiCalculation(calculationsManager.getFreeThread(i)));
        }
    }

    public void start() {
        for (final Runnable piThread : threadForPiCalculations) {
            new Thread(piThread).start();
        }
    }

}
