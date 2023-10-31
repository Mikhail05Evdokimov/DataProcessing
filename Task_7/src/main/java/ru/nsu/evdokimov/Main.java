package ru.nsu.evdokimov;

import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main {

    public static CyclicBarrier barrier;

    public static void main (String[] args) throws InterruptedException, BrokenBarrierException {
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
