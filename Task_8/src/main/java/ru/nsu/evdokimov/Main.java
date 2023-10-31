package ru.nsu.evdokimov;

import sun.misc.SignalHandler;

import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class Main {

    public static CyclicBarrier barrier;
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
        barrier = new CyclicBarrier(departmentsCount + 1);
        CalculationsManager calculationsManager = new CalculationsManager(departmentsCount);
        ThreadsManager threadsManager = new ThreadsManager(calculationsManager);
        threadsManager.start();
        barrier.await();
        calculationsManager.showCollaborativeResult();
    }

}
