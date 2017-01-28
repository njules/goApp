package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.junit.Assert.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Timer;

import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.EventDeletionTimer;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public class EventDeletionTimerTest {
    private static final int SECONDS_TOLERANCE = 10;

    @Test
    public void test() {
        String name = "testn";
        Location location = new Location(0, 0, name);
        User user = new UserManagement().add(name, 264212);
        assertNotNull(user);
        Group group = new GroupManagement().add(name, user.getUserId());
        assertNotNull(group);
        Timestamp time = new Timestamp(System.currentTimeMillis() + 1000);
        EventManagement management = new EventManagement();
        Event event = management.add(name, location, time, user.getUserId(), group.getGroupId());
        assertNotNull(event);

        new EventDeletionTimer(60, 0);
        for (int i = 0; i < 60 + SECONDS_TOLERANCE; i++) {
            if (i < 60 - SECONDS_TOLERANCE) {
                assertNotNull(management.getEvent(event.getEventId()));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        assertNull(management.getEvent(event.getEventId()));
    }

}
