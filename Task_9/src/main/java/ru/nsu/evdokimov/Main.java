package ru.nsu.evdokimov;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final List<Fork> forkList = new ArrayList<>();
    private static final List<Philosopher> philosophers = new ArrayList<>();

    public static void main (String[] args) {

        for (int i = 1; i < 6; i++) {
            forkList.add(new Fork(i));
        }

        philosophers.add(new Philosopher(1, 4, forkList.get(0), forkList.get(1)));
        philosophers.add(new Philosopher(2, 4, forkList.get(1), forkList.get(2)));
        philosophers.add(new Philosopher(3, 4, forkList.get(2), forkList.get(3)));
        philosophers.add(new Philosopher(4, 4, forkList.get(3), forkList.get(4)));
        philosophers.add(new Philosopher(5, 4, forkList.get(4), forkList.get(0)));

        for (Philosopher i : philosophers) {
            i.start();
        }
    }

}