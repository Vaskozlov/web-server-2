package org.vaskozlov.web2.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.concurrent.atomic.AtomicInteger;

@WebListener
public class SessionListener implements HttpSessionListener {
    private final AtomicInteger activeSessions = new AtomicInteger(0);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.printf(
                "Session with id %s has been created. Active sessions count: %d%n",
                se.getSession().getId(),
                activeSessions.addAndGet(1)
        );
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.printf(
                "Session with id %s has been destroyed. Active sessions count: %d%n",
                se.getSession().getId(),
                activeSessions.addAndGet(-1)
        );
    }

    public int getActiveSessions() {
        return activeSessions.get();
    }
}
