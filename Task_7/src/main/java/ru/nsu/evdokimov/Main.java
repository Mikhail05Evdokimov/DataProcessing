package ru.nsu.evdokimov;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main {

    public static CyclicBarrier berrier;

    public static void main (String[] args) throws InterruptedException, BrokenBarrierException, IOException {
        Scanner scan = new Scanner(System.in);
        int departmentsCount = scan.nextInt();
        berrier = new CyclicBarrier(departmentsCount + 1);
        Company company = new Company(departmentsCount);
        Founder founder = new Founder(company);
        founder.start();
        berrier.await();
        company.showCollaborativeResult();
    }

}
