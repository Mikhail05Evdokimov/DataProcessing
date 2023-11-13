package ru.nsu.evdokimov;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Fork {
    private final int id;
    private final Lock lock;

    public Fork(int id) {
        this.id = id;
        lock = new ReentrantLock();
    }

    public int getId() {
        return id;
    }

    public boolean tryLock() {
        return lock.tryLock();
    }

    public boolean getFork() {
        synchronized (lock) {
            if(lock.tryLock()) {
                lock.lock();
                return true;
            }
            else {
                return false;
            }
        }
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }
}
