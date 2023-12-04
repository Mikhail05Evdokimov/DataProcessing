package ru.nsu.evdokimov;

import sun.misc.SignalHandler;

import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main {

    //Threads tell Main that they finished calculations.
    public static CyclicBarrier firstAmortizationBarrier;
    //Main tells threads the maximum iteration that they have to reach.
    public static CyclicBarrier secondAmortizationBarrier;
    //Threads tell Main that they finished extra iterations.
    public static CyclicBarrier barrier;
    //Becomes False after SIGINT
    public static volatile Boolean resource = true;

    public static void main (String[] args) throws InterruptedException, BrokenBarrierException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                SignalHandler signalHandler = sig -> resource = false;
                StopSignalHandler.install("INT", signalHandler);
            }
        };
        thread.start();

        Scanner scan = new Scanner(System.in);
        int departmentsCount = scan.nextInt();
        int departmentsCountPlusOne = departmentsCount + 1;
        barrier = new CyclicBarrier(departmentsCountPlusOne);
        firstAmortizationBarrier = new CyclicBarrier(departmentsCountPlusOne);
        secondAmortizationBarrier = new CyclicBarrier(departmentsCountPlusOne);
        CalculationsManager calculationsManager = new CalculationsManager(departmentsCount);
        ThreadsManager threadsManager = new ThreadsManager(calculationsManager);
        threadsManager.start();
        firstAmortizationBarrier.await();

        int max = 0;
        for (ThreadForPiCalculation i : threadsManager.getThreads()) {
            int iteration = i.getMultiThreadPiCalculator().getCurrentIteration();
            if (iteration > max) {
                max = iteration;
            }
        }

        for (ThreadForPiCalculation i : threadsManager.getThreads()) {
            i.getMultiThreadPiCalculator().setMaxIteration(max);
        }

        secondAmortizationBarrier.await();

        barrier.await();
        calculationsManager.showCollaborativeResult();
    }

}