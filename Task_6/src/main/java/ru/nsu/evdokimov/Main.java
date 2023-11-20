package ru.nsu.evdokimov;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main {

    private static final int departmentsCount = 10;
    public final static CyclicBarrier berrier = new CyclicBarrier(departmentsCount + 1);

    public static void main (String[] args) throws InterruptedException, BrokenBarrierException {
        Company company = new Company(departmentsCount);
        Founder founder = new Founder(company);
        founder.start();
        berrier.await();
        company.showCollaborativeResult();
    }

}
