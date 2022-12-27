package com.vadvincho.kafka.lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantCountLock extends ReentrantLock {

    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }

    public boolean isNotActual() {
        return count.get() == 0;
    }

    @Override
    public void unlock() {
        super.unlock();
        count.decrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}
