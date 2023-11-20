package ru.nsu.evdokimov;

public class Worker implements Runnable {
    private final Department department;

    public Worker(Department department) {
        this.department = department;
    }

    @Override
    public void run() {
        department.performCalculations();
    }
}
