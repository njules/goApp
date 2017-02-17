package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;

import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.EventDeletionTimer;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public class EventDeletionTimerTest {
    private static final int SECONDS_TOLERANCE = 2;

    @Test
    public void test() {
        String name = "testn";
        Location location = new Location(0, 0, name);
        User user = new UserManagement().add(name, "264212");
        assertThat(user, is(notNullValue()));
        Group group = new GroupManagement().add(name, user.getUserId());
        assertThat(group, is(notNullValue()));
        Timestamp time = new Timestamp(System.currentTimeMillis() + 1000);
        EventManagement management = new EventManagement();
        Event event = management.add(name, location, time, user.getUserId(), group.getGroupId());
        assertThat(event, is(notNullValue()));

        new EventDeletionTimer(600, 0);
        for (int i = 0; i < 6 + SECONDS_TOLERANCE; i++) {
            if (i < 6 - SECONDS_TOLERANCE) {
                assertThat(management.getEvent(event.getEventId()), is(notNullValue()));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        assertThat(management.getEvent(event.getEventId()), is(nullValue()));
    }

}
