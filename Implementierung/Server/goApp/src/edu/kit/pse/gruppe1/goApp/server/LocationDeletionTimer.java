package edu.kit.pse.gruppe1.goApp.server;

import java.util.Timer;
import java.util.TimerTask;

import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;

/**
 * 
 * Run in background and delets old Locations.
 *
 */
public class LocationDeletionTimer extends TimerTask {
    private Timer timer;

    /**
     * Starts the timer which calls the run method
     * 
     * @param excecutionsPerHour
     *            The number of times the timer calls the run-method in an hour.
     */
    public LocationDeletionTimer(double excecutionsPerHour) {
        timer = new Timer();
        // period represents the period between each timer execution (call of run method")
        long period = (long) (60 * 60 * 1000.0 / excecutionsPerHour);
        timer.schedule(this, 200, period);
    }

    /**
     * stops the timer
     */
    public void stopTimer() {
        timer.cancel();
        this.cancel();
    }

    @Override
    public void run() {
        new UserManagement().deleteOldLocations();
    }
}
