package edu.kit.pse.gruppe1.goApp.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class TimerStarter implements ServletContextListener {

    private EventDeletionTimer eventDeletionTimer;
    private LocationDeletionTimer locationDeletionTimer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        eventDeletionTimer = new EventDeletionTimer(30, 60);
        locationDeletionTimer = new LocationDeletionTimer(30);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        eventDeletionTimer.stopTimer();
        locationDeletionTimer.stopTimer();

    }

}
