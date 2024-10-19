package org.vaskozlov.web2.service;

import jakarta.servlet.ServletContext;
import org.vaskozlov.web2.lib.AutoReadUnlock;
import org.vaskozlov.web2.lib.AutoWriteUnlock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ContextSynchronizationService {
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    public static Object readFromContext(ServletContext context, String fieldName) {
        try (var ignored = applyReadLock()) {
            return context.getAttribute(fieldName);
        }
    }

    public static AutoReadUnlock applyReadLock() {
        return AutoReadUnlock.lock(ContextSynchronizationService.lock);
    }

    public static AutoWriteUnlock applyWriteLock() {
        return AutoWriteUnlock.lock(ContextSynchronizationService.lock);
    }
}

