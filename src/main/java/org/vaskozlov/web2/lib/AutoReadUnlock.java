package org.vaskozlov.web2.lib;

import java.util.concurrent.locks.ReadWriteLock;

public class AutoReadUnlock implements AutoCloseable {
    private final ReadWriteLock lock;

    public static AutoReadUnlock lock(ReadWriteLock lock) {
        lock.readLock().lock();
        return new AutoReadUnlock(lock);
    }

    private AutoReadUnlock(ReadWriteLock lock) {
        this.lock = lock;
    }

    @Override
    public void close() {
        lock.readLock().unlock();
    }
}
