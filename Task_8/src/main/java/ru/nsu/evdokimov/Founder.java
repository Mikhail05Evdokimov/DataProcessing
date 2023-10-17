package ru.nsu.evdokimov;

import java.util.ArrayList;
import java.util.List;

public final class Founder {

    private final List<Worker> workers;
    public Founder(final Company company) {
        this.workers = new ArrayList<>();
        for (int i = 0; i < company.getDepartmentsCount(); i++) {
            workers.add(new Worker(company.getFreeDepartment(i)));
        }
    }

    public void start() {
        for (final Runnable worker : workers) {
            new Thread(worker).start();
        }
    }

}
