package org.vaskozlov.web2.service;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.vaskozlov.web2.lib.AutoReadUnlock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ContextSynchronizationService {
    public static final ReadWriteLock lock = new ReentrantReadWriteLock();

    public static Object readFromContext(HttpServletRequest request, String fieldName) {
        try (var ignored = AutoReadUnlock.lock(ContextSynchronizationService.lock)) {
            final ServletContext context = request.getServletContext();
            return context.getAttribute(fieldName);
        }
    }
}

