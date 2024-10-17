package org.vaskozlov.web2.lib;

import java.util.concurrent.locks.ReadWriteLock;

public class AutoWriteUnlock implements AutoCloseable {
    private final ReadWriteLock lock;

    public static AutoWriteUnlock lock(ReadWriteLock lock) {
        lock.writeLock().lock();
        return new AutoWriteUnlock(lock);
    }

    private AutoWriteUnlock(ReadWriteLock lock) {
        this.lock = lock;
    }

    @Override
    public void close() {
        lock.writeLock().unlock();
    }
}
