package edu.kit.pse.gruppe1.goApp.server;

import java.util.Timer;
import java.util.TimerTask;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventManagement;

public class EventDeletionTimer extends TimerTask {
    private long minutesTillDeletion;
    private Timer timer;
    private String textToDisplay;

    public EventDeletionTimer(double excecutionsPerHour, long minutesTillDeletion) {
        this.minutesTillDeletion = minutesTillDeletion;
        timer = new Timer();
        // period represents the period between each timer execution (call of run method")
        long period = (long) (60 * 60 * 1000.0 / excecutionsPerHour);
        timer.schedule(this, 200, period);
    }

    @Override
    public void run() {
        new EventManagement().deleteOldEvents(minutesTillDeletion);
    }
}